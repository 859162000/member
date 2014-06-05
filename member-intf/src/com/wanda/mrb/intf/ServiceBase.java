package com.wanda.mrb.intf;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.omg.CORBA.SystemException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.icebean.core.conn.db.DBConnManager;
import com.wanda.mrb.intf.bean.XmlHeadBean;
import com.wanda.mrb.intf.exception.BusinessException;
import com.wanda.mrb.intf.exception.SysException;
import com.wanda.mrb.intf.utils.PwdUtil;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

/**
 * This class is the superclass for all business servlet interfaces
 */
public abstract class ServiceBase{
	private static final long serialVersionUID = 1L;
	protected final org.apache.log4j.Logger log = Logger.getLogger(this.getClass());
	public String intfCode="";
	public String memberNo="";
	private long reqid;
	private XmlHeadBean intfHeadBean=null;
	protected Connection dbConnection = null;
	protected Vector<Connection> dbConnectionList = new Vector<Connection>();
	
	protected boolean timeOutFlag=false;//是否处理超时，默认不处理超时
	protected long startTime = -1;	
	protected long endTime=0;
	protected long timeoutvalue;
	protected long timeLimit=5000;//默认超时为5秒
	protected String cinemaCode = "";  //调用请求来自哪里t_intf_clientinfo.cinemacode

	public ServiceBase() {
		super();
		intfHeadBean=new XmlHeadBean();
	}
	
	/**
	 * 	1. 完成业务逻辑
	 * 	2. 设置处理结果码respCode
	 */
	protected abstract void bizPerform() throws Exception;
	
	/**
	 * 获取输入xml中业务参数
	 * 如果参数不存在，或参数值长度超过最大长度，抛出例外
	 */
	protected abstract void parseXMLParam(Element root) throws Exception;

	/**
	 * 组织返回xml的body体字符串
	 */
	protected abstract String composeXMLBody();
	
	/**
	 * 每个继承的Business Servlet中调用的方法，
	 * 逻辑 : 
	 * 	从HTTP REQUEST中取用户请求参数，
	 * 	进行IP校验
	 * 	进行用户名/密码校验
	 *  进行业务处理
	 *  进行日志处理
	 *  释放资源
	 *  返回xml结果 
	 */
	public StringBuffer defaultProc(HttpServletRequest request,	HttpServletResponse response)
		throws javax.servlet.ServletException, java.io.IOException {
		String xmlParamValue=getParameter(request,ConstDef.REQ_PARAM_XML,"");//获取xml参数值
		startTime = System.currentTimeMillis();
		getIpAddr(request);
		if("".equals(xmlParamValue)){
			return null;//非正常请求
		}
		reqid=ConstDef.REQ_ID.getAndIncrement();
		if(reqid+10000==Long.MAX_VALUE){ConstDef.REQ_ID.set(1);}//防止超出Long最大值
		if (log.isInfoEnabled()) log.info("req:"+getIpAddr(request)+",id_"+reqid+",xml:{"+xmlParamValue.trim().replaceAll("\r\n","").replaceAll("\r","").replaceAll("\n","")+"}");

		StringBuffer xmlResponse = new StringBuffer();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		try {
			db = dbf.newDocumentBuilder();
			Document d = db.parse(new ByteArrayInputStream(xmlParamValue.getBytes("UTF-8")));
			Element root = d.getDocumentElement();
			parseCommonRequestParams(root, request); //xml中head部分的解析
			this.dbConnection=this.getDBConnection();
			
			/**验证ip 用户名密码*/
			ResultQuery rq=SqlHelp.query(this.dbConnection, SQLConstDef.SELECT_CLIENTINFO, this.intfHeadBean.getIpAddress(),this.intfHeadBean.getUsername());
			ResultSet rs=rq.getResultSet();
			if (rs!=null && rs.next()) {
				if(!passwordValidate(this.intfHeadBean.getPasswd(),rs.getString("CONNPWD"))){
					throwsBizException("PT90000", "IP未授权");                                              
				}
				cinemaCode = rs.getString("CINEMACODE");
			}else{
				throwsBizException("PT90000", "IP未授权");                                              
			}
			if(!this.intfCode.equals(this.intfHeadBean.getIntfCode())){
				throwsBizException("PT90000", "地址接口编码不匹配");                                              
			}
			parseXMLParam(root); //参数解析:业务参数放到各业务接口

			bizPerform();// 业务处理&业务日志
			this.timeOfRequest();
			this.intfHeadBean.setRetCode(ReCodeDef.CONST_RESPCODE_SUCCESS);//设置成功标志
		} catch (BusinessException e) {
			this.intfHeadBean.setRetInfo(e.getRespCode(), e.getMessage());
			//e.printStackTrace();
		} catch (Exception e) {
			this.intfHeadBean.setRetInfo(ReCodeDef.RESPCODE_OTHER_EXCEPTION, e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			this.intfHeadBean.setRetInfo(ReCodeDef.RESPCODE_OTHER_EXCEPTION, t.getMessage());
			t.printStackTrace();
		} finally {
			if (!"0".equals(this.intfHeadBean.getRetCode())&&dbConnection!=null){
				try {
					dbConnection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else if(dbConnection!=null){
				try {
					dbConnection.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				freeResource();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		xmlResponse = composeXML();
		if (log.isInfoEnabled()) log.info("resp:id_"+reqid+",xml:{"+ xmlResponse.toString().replaceAll("\r\n","").replaceAll("\r","").replaceAll("\n","")+"}");
		dbf = null;
		return xmlResponse;
	}
	
	public void freeResource() throws SystemException {
		try {
			if (dbConnectionList != null) {
				for (int i = 0; i < dbConnectionList.size(); i++) {
					try {
						((Connection) dbConnectionList.elementAt(i)).close();
					} catch (Exception ex1) {
					}
				}
			}
		} catch (Exception ex) {
		}

		dbConnectionList = null;

		//释放当前的数据库连接
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (Exception ex) {
			}
		}
		dbConnection = null;
	}
	public Connection getDBConnection() throws SysException {
		try {
			if (dbConnection.isClosed())
				dbConnection = null;
		} catch (Exception ex) {
		}

		if (dbConnection == null) {
			try {
				dbConnection = DBConnManager.getConnection();//ConnPoolHandler.getHandle().getConnection();
				dbConnection.setAutoCommit(false);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new SysException(ReCodeDef.RESPCODE_GET_DBCONNECTION_FAILED,ex);
			}
		}

		return dbConnection;
	}

	/**
	 * 根据参数名得到参数值
	 * @return java.lang.String
	 * @param paramName java.lang.String
	 */
	public String getParameter(HttpServletRequest request, String paramName, String defaultValue){
		String paramValue = null;
		try {
			request.setCharacterEncoding("UTF-8"); 
			paramValue = request.getParameter(paramName);
		} catch (UnsupportedEncodingException e) {
		}
		if (paramValue == null) paramValue = defaultValue;
		//if (log.isDebugEnabled()) log.debug("xml="+paramValue+"|");
		return paramValue;
	}

	/**
	 * 向应用层提供接口，用户抛出业务异常
	 */
	public static void throwsBizException(String respCode, String message)throws BusinessException {
		throw new BusinessException(respCode, message);
	}
	
	/**
	 * 解析请求参数
	 * @param root
	 * @param childName
	 * @param maxLen
	 * @return
	 * @throws BusinessException
	 */
	protected String getChildValueByName(Element root, String childName, int maxLen) throws BusinessException{
		NodeList nl = root.getChildNodes();
		String value = null;
		for (int i = 0 ; i < nl.getLength() ; i++){
			Node node = nl.item(i);
			if (childName.equals(node.getNodeName())) {
				value = "";
				try {
					value = node.getFirstChild().getNodeValue().trim();
				}catch(Exception e) {
					value = "";
				}
				 break;
			} 
		}
		
		if (value == null) {
			if (log.isDebugEnabled()) log.debug("Param:"+childName+" not exist!");
			throwsBizException(ReCodeDef.CONST_RESPCODE_LACK_OF_PARAM, childName);
		}

		if (value.length() > maxLen) {
			if (log.isDebugEnabled()) log.debug("Param:"+childName+" length is "+value.length()+ ">"+maxLen);
			throwsBizException(ReCodeDef.CONST_RESPCODE_PARAM_LENGTH_ERROR, childName);
		}
		return value;
	}

	/**
	 * xml中head部分的解析
	 */
	private void parseCommonRequestParams(Element root, HttpServletRequest request) throws BusinessException{
		this.intfHeadBean.setIntfCode(this.intfCode);
		this.intfHeadBean.setIntfVer(root.getAttribute(ConstDef.XML_ATTR_INTFVER));
		this.intfHeadBean.setClientType(root.getAttribute(ConstDef.XML_ATTR_CLIENTTYPE));
		this.intfHeadBean.setSerialNum(root.getAttribute(ConstDef.XML_ATTR_SERIALNUM));
		this.intfHeadBean.setUsername(root.getAttribute(ConstDef.XML_ATTR_USERNAME));
		this.intfHeadBean.setPasswd(root.getAttribute(ConstDef.XML_ATTR_PASSWD));
		this.intfHeadBean.setIpAddress(this.getIpAddr(request));
		validateParamNotEmpty(this.intfHeadBean.getIntfCode(),   ConstDef.XML_ATTR_INTFCODE);
		validateParamNotEmpty(this.intfHeadBean.getIntfVer(),    ConstDef.XML_ATTR_CLIENTTYPE);
		validateParamNotEmpty(this.intfHeadBean.getClientType(), ConstDef.XML_ATTR_CLIENTTYPE);
		validateParamNotEmpty(this.intfHeadBean.getSerialNum(),  ConstDef.XML_ATTR_SERIALNUM);
		validateParamNotEmpty(this.intfHeadBean.getUsername(),   ConstDef.XML_ATTR_USERNAME);
		validateParamNotEmpty(this.intfHeadBean.getPasswd(),     ConstDef.XML_ATTR_PASSWD);
	}
	
	/**
	 * 构造完整的返回xml
	 * @return
	 */
	protected StringBuffer composeXML() {
		StringBuffer buf = new StringBuffer();
		buf.append(composeXMLHeadByRetCode());
		if (this.intfHeadBean.isRetCodeSuccess())
			buf.append(composeXMLBody());
		buf.append(composeXMLTail());
		
		return buf;
	}
	
	/**
	 * 构建返回头
	 */
	protected String composeXMLHeadByRetCode() {
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version='1.0' encoding='UTF-8'?>");
		buf.append("<ccsresp");
		buf.append(" icode=\""+this.intfHeadBean.getIntfCode());
		buf.append("\" serialnum=\""+this.intfHeadBean.getSerialNum());
		buf.append("\" retcode=\""+this.intfHeadBean.getRetCode());
		buf.append("\" retmsg=\"");
			if (!this.intfHeadBean.isRetCodeSuccess()) {
				buf.append(this.intfHeadBean.getRetMsg());
			}
		buf.append("\">");
		return buf.toString();
	}
	
	protected String composeXMLTail(){
		return "</ccsresp>";
	}

	/**
	 * 检查业务访问是否超出响应时间
	 * @param intfCode
	 * @param adapter
	 * @return
	 * @throws BusinessException 
	 */
	protected void timeOfRequest() throws BusinessException {
		endTime = System.currentTimeMillis();
		timeoutvalue = endTime - startTime;
		if(timeOutFlag&&timeoutvalue > timeLimit){
			throwsBizException("PT91004","请求处理超时");
		}
	}
	
	/**
	 * 验证密码
	 * @param reqPasswd
	 * @param dbPwd
	 * @return
	 * @throws Exception
	 */
	public boolean passwordValidate(String reqPasswd, String dbPwd)throws Exception {
		String plainPwd = PwdUtil.decryptByPrivateKey(reqPasswd);
		String md5Pwd = PwdUtil.md5Encrypt(plainPwd); //encoder后的字符串一般会按照每76位加一个回车符，整个字符串结束后还会加一个回车换行符
		if (log.isDebugEnabled()) log.debug("plain="+plainPwd+"|md5="+md5Pwd+"|dbpwd="+dbPwd);
		if (!md5Pwd.startsWith(dbPwd)) { //md5Pwd比数据库中的md5多两个字符(回车换行)
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 获取IP地址 负载均衡取最后一个ip地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) { 
		String ip = request.getHeader("x-forwarded-for"); 
		
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}else {
			int idx = ip.lastIndexOf(",");
			if (idx>0) {
				try {
					ip = ip.substring(idx+1);
				} catch (Exception e) { }
			}
		}
		return ip; 
	}
	protected void validateParamNotEmpty(String paramValue, String paramName) throws BusinessException {
		if ("".equals(paramValue)) {
			throw new BusinessException(ReCodeDef.CONST_RESPCODE_PARAM_EMPTY, paramName);
		}
	}
	
	protected String createXmlTag(String tagName,String value){
		//特殊字符转义
		value = StringEscapeUtils.escapeXml(value);
		//生成返回XML
		return "<"+tagName+">"+value+"</"+tagName+">";
	}
	
	public int checkMember(Connection conn,String... memberNo) throws BusinessException, SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int memberId=0;
		ps = conn.prepareStatement(SQLConstDef.CHECK_MEMBER);
		if(memberNo!=null&&memberNo.length!=0){
			ps.setString(1, memberNo[0]);
		}else{
			ps.setString(1, this.memberNo);
		}
		rs = ps.executeQuery();
		if(rs ==null || !rs.next()){
			throwsBizException("M0001", "会员不存在");
		}else{
			memberId=rs.getInt("MEMBER_ID");
		}
		return memberId;
	}
	
	public void checkMobileNO(Connection conn,String mobileNO) throws BusinessException, SQLException {
		//校验手机号是否合法
		String regExp = "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobileNO);
		if(!m.find()){
			throwsBizException("M010001","手机号不正确");
		}
		//校验手机号是否已经被注册
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(SQLConstDef.MEMBER_CHECK_MOBILE_NO);
		ps.setString(1, mobileNO);
		rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			throwsBizException("M010002","此手机号系统中已经存在，请重新输入!");
		}
	}
	public void checkEmail(Connection conn,String email) throws BusinessException{
		//校验邮箱地址是否合法
		String regExp = "(^[\\S]{0,0}$)|(^[\\S]*@[\\S]*.[\\S]*$)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(email);
		if(!m.find()){
			throwsBizException("M010003","邮箱不正确");
		}
	}
}
