package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mms.control.stream.service.IntegralInitialization;
import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.GetEventCmn;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.exception.BusinessException;
import com.wanda.mrb.intf.member.vo.TContent;
import com.wanda.mrb.intf.member.vo.TMember;
import com.wanda.mrb.intf.utils.FormatTools;
import com.wanda.mrb.intf.utils.MemberUtils;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.Utils;

/**
 * @author xuesi
 * 会员注册
 */
public class Register extends ServiceBase {
	public Register() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_REGISTER;
		this.timeOutFlag = true;
	} 

	TMember member = new TMember();
	long memberSeqId = 0;
	String triggerSystem = "";
	String cinemaCode = "";//注册影城
	String cinemaInnerCode = "";//发短信是传的影城内码
	String favCinemaCode = "";//常驻影城
	String registOpSystem = "";
	MemberUtils memberUtil = new MemberUtils();
	TContent content = new TContent();
	GetEventCmn gec = new GetEventCmn();

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		PreparedStatement ps = null;
		try {
			// 验证手机号
			checkMobileNO(conn, member.mobile);
			checkEmail(conn, member.email);
			
			//当注册影城为空，常驻影城不为空时，是来自网站的注册
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_MEMBERID);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				memberSeqId = rs.getLong("NEXTVAL"); // 获取主键
			}
			//根据影城编码获得到影城id
			ps = conn.prepareStatement(SQLConstDef.MEMBER_SELECT_CINEMAID_BY_CODE);
			if (!"".equals(cinemaCode)) {
				// 获取会员编码
				member.memberNo = memberUtil.getMemberNO(conn, cinemaCode);
				ps.setString(1, cinemaCode);
			} else if (!"".equals(favCinemaCode)) {
				//网站注册常驻影城变为会员注册影城
				favCinemaCode = memberUtil.randomCinema(conn,favCinemaCode);
				 //获取会员编码
				member.memberNo = memberUtil.getMemberNO(conn, favCinemaCode);
				ps.setString(1, favCinemaCode);
			} else {
				throw new BusinessException("M010004", "网站常驻影城不能为空，POS注册影城不能为空！");
			}
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				if(super.cinemaCode != null && !"99999999".equals(super.cinemaCode)){
					member.registCinemaId = rs.getLong("seqid"); //POS注册直接获取注册影城
				}else{
					member.registCinemaId = 0L; //网站注册注册影城为空，计算管理影城时把管理影城回推为注册影城
				}
				
			}
			
			if(super.cinemaCode != null && !"99999999".equals(super.cinemaCode)){
				//POS
				registOpSystem = "POS";
			} else {
				//WEB
				registOpSystem = "others";
			}
			
			
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER);
			ps.setLong(1, memberSeqId);
			ps.setString(2, member.memberNo);// 会员编码产生的规则
			ps.setString(3, member.mobile);//
			ps.setString(4, member.name);//
			ps.setString(5, "".equals(member.gender)?"O":member.gender);//
			ps.setDate(6, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
			ps.setLong(7, 1);// 注册时默认为新会员; 注册类型，需要接口中定义该字段 1:主动注册;2:自动转换;3:批量导入
			ps.setString(8, member.registChnID);//招募渠道
			ps.setString(9, member.operator);//
			ps.setString(10, member.operatorName);// 注册员工姓名
			ps.setLong(11, member.registCinemaId);// 
			ps.setLong(12, 1);// 会员状态，注册是设置成有效 1有效 0无效
			ps.setInt(13, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
			ps.setString(14, member.operator);
			ps.setString(15, member.operator);
			ps.setString(16, member.email);
//			ps.setLong(17, member.iscontactable);
			ps.setString(17, member.tel);
			ps.setLong(18, member.registCinemaId);
			ps.setString(19, member.registDate);
			ps.setString(20, member.dtsId);//会员来源
			ps.setString(21, registOpSystem);//会员来源
			ps.setString(22, member.arrivalType);//交通方式
			ps.setString(23, member.oftenChannel);//常用购票渠道
			ps.setString(24,member.registChnExtID);//招募渠道扩展
			ps.execute();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}

		// 常用信息
		try {
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_INFO);
			ps.setLong(1, member.registCinemaId);
			ps.setInt(2, member.education);
			ps.setInt(3, member.occupation);
			ps.setInt(4, member.income);
			ps.setString(5, "".equals(member.maritalStatus)?"O":member.maritalStatus);
			ps.setInt(6, member.childNumber);
			ps.setLong(7, member.fqCinemaDist);
			ps.setLong(8, member.fqCinemaTime);
			ps.setString(9, "".equals(member.mobileOptin)?"O":member.mobileOptin);
			ps.setInt(10, member.identityType);
			ps.setString(11, member.idCardNo);
//			ps.setString(12, member.idCardHashNo);
			ps.setInt(12, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
			ps.setString(13, member.operator);
			ps.setString(14, member.operator);
			ps.setLong(15, memberSeqId);
			ps.setString(16, member.otherNo);
			ps.execute();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		// 地址信息
		try {
			//先通过注册影城获取到省，市
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_PROVINCE_CITY);
			ps.setLong(1, member.registCinemaId);
			ResultSet rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				member.provinceID = rs.getLong("PROVINCE");
				member.cityID = rs.getLong("CITY");
			}
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_ADDR);
			ps.setString(1, member.zipCode);
			ps.setString(2, member.address1);
			ps.setLong(3, (member.provinceID==null)?0L:member.provinceID);
			ps.setLong(4, (member.cityID==null)?0L:member.cityID);
			ps.setInt(5, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
			ps.setString(6, member.operator);
			ps.setString(7, member.operator);
			ps.setLong(8, memberSeqId);
			ps.execute();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		// 喜欢看的电影
		try {
			ps = conn
					.prepareStatement(SQLConstDef.MEMBER_REGISTER_FAV_FILMTYPE);
			if("".equals(member.favFilmType) || member.favFilmType == null){
				ps.setString(1, "1");
			}else {
				ps.setString(1, member.favFilmType);
			}
			ps.setInt(2, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
			ps.setString(3, member.operator);
			ps.setString(4, member.operator);
			ps.setLong(5, memberSeqId);
			ps.execute();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		// 喜欢的联系方式
		try {
			ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_FAV_CONTACT);
			if("".equals(member.contactMeans) || member.contactMeans == null){
				ps.setString(1, "4");
			}else {
				ps.setString(1, member.contactMeans);
			}
			ps.setInt(2, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
			ps.setString(3, member.operator);
			ps.setString(4, member.operator);
			ps.setLong(5, memberSeqId);
			ps.execute();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		// 处理积分
		// 处理会员级别
		IntegralInitialization initPoint = new IntegralInitialization();
		initPoint.addMemberPointByID(conn, memberSeqId, member.registOpNo, 0);

		//记录会员创建日志
		SqlHelp.operate(conn, SQLConstDef.INSERT_MEMBER_LOG,
				""+memberSeqId,member.mobile,member.operator,member.operator,member.operator,member.birthday);
		
		// 注册完成之后返回注册信息
		try {
			ResultSet rs = null;
			ps = conn.prepareStatement(SQLConstDef.MEMBER_SELECT_INFO);
			ps.setLong(1, memberSeqId);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				member.memberNo = rs.getString("MEMBER_NO");
				member.levelPointTotal = Integer.parseInt(rs
						.getString("LEVEL_POINT_TOTAL"));
				member.activityPoint = Integer.parseInt(rs
						.getString("ACTIVITY_POINT"));
				member.exgPointBalance = Integer.parseInt(rs
						.getString("EXG_POINT_BALANCE"));
				member.levelCode = rs.getString("MEM_LEVEL");
				member.expireDate = rs.getString("EXPIRE_DATE");
				member.orgLevel = rs.getString("ORG_LEVEL");
				member.setTime = rs.getString("SET_TIME");
				member.targetLevel = rs.getString("TARGET_LEVEL");
				member.levelPointOffset = rs.getString("LEVEL_POINT_OFFSET");
			}
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		
		//获取短信平台代理地址和通道号
//		String msgSvcIp = "";
//		String msgChannelId = "";
//		String msgRegOpen = "";
//		try {
//			ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//			ps.setString(1, "MSG_MQ_IP");
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				msgSvcIp = rs.getString("parameter_value");
//			}
//			
//			ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//			ps.setString(1, "MSG_CHANNEL_ID");
//			rs = ps.executeQuery();
//			while (rs.next()) {
//				msgChannelId = rs.getString("parameter_value");
//			}
//			
//			ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//			ps.setString(1, "MSG_REG_OPEN");
//			rs = ps.executeQuery();
//			while (rs.next()) {
//				msgRegOpen = rs.getString("parameter_value");
//			}
//		} catch (Exception e) {
//			conn.rollback();
//			throw e;
//		}
//		Map<String,String> msgConfigMap = SmsConfigFactory.getSmsConfigInstance(conn);
//		msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
//		msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
//		msgRegOpen = msgConfigMap.get("MSG_REG_OPEN");
		
		//发送欢迎短信
/*		if(super.cinemaCode != null && !"99999999".equals(super.cinemaCode)){
			//POS发送短信
			triggerSystem = "POS";
			content = gec.getRegEvenContent(conn, triggerSystem);
			ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE, super.cinemaCode);
			ResultSet rs=rsq.getResultSet();
			while (rs.next()) {
				cinemaInnerCode = rs.getString("INNER_CODE");
			}
			//SendCheckCodeMsg.sendMsgCheckCode(member.mobile,cinemaInnerCode,content.getPosMsgContent());
//			SMSControl smsSendObj = new SMSControl();
//			smsSendObj.smssend(conn, member.mobile, content.getPosMsgContent());
			if ("1".equals(msgRegOpen)) {
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId, member.mobile, cinemaInnerCode, content.getPosMsgContent());
			}
		} else {
			//WEB发送短信
			triggerSystem = "WEB";
			cinemaInnerCode = "002";
			content = gec.getRegEvenContent(conn, triggerSystem);
			//SendCheckCodeMsg.sendMsgCheckCode(member.mobile,cinemaInnerCode,content.getWebMsgContent());
//			SMSControl smsSendObj = new SMSControl();
//			smsSendObj.smssend(conn, member.mobile, content.getWebMsgContent());
			if ("1".equals(msgRegOpen)) {
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId, member.mobile, cinemaInnerCode, content.getWebMsgContent());
			}
		}*/
		//话术
		member.talk = gec.composePosRegXML(content);
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		try {
			try {
				//常用购票渠道
				member.oftenChannel = getChildValueByName(root,"OFTEN_CHANNEL", 15);
				// 到达影城方式 交通方式
				member.arrivalType = getChildValueByName(root,"ARRIVAL_TYPE", 2);
			} catch (Exception e) {
				System.out.println("OFTEN_CHANNEL,ARRIVAL_TYPE is not writed");
			}
			// 姓名
			member.name = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_NAME, 50);
			// 手机号
			member.mobile = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_MOBILE, 11);
			//固定电话
			member.tel = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_TEL, 30);
			//证件号码，掩码
			member.idCardNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_NO, 18);
			//证件号码，MD5码
//			member.idCardHashNo = getChildValueByName(root,
//					ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_HASHNO, 32);
			//证件类型
			String identityTypeStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_IDENTITY_TYPE, 8);
			if (!"".equals(identityTypeStr)) {
				member.identityType = Integer.parseInt(identityTypeStr);
			}
			//邮件地址
			member.email = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_EMAIL, 50);
			//地址
			member.address1 = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_ADDRESS, 150);

			String provinceIDStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_PROVINCEID, 30);
			if (!"".equals(provinceIDStr)) {
				member.provinceID = Long.parseLong(provinceIDStr);
			}
			String cityIDStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_CITYID, 30);
			if (!"".equals(cityIDStr)) {
				member.cityID = Long.parseLong(cityIDStr);
			}
			member.zipCode = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_ZIP, 10);
			member.contactMeans = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_CONTACTMEANS, 30);
			member.birthday = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_BIRTHDAY, 50);
			if (!"".equals(member.birthday)) {
				if(!Utils.validDate(member.birthday)){
					throwsBizException("M010005", "生日格式应为：YYYY-MM-DD！");
				}
			}
			member.gender = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_GENDER, 8);
			member.maritalStatus = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_MARITALSTATUS, 8);

			String childNumberStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_CHILDNUMBER, 8);
			if (!"".equals(childNumberStr)) {
				member.childNumber = Integer.parseInt(childNumberStr);
			}
			String educationStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_EDUCATION, 8);
			if (!"".equals(educationStr)) {
				member.education = Integer.parseInt(educationStr);
			}
			String occupationStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OCCUPATION, 8);
			if (!"".equals(occupationStr)) {
				member.occupation = Integer.parseInt(occupationStr);
			}
			String incomeStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_INCOME, 20);
			if (!"".equals(incomeStr)) {
				member.income = Integer.parseInt(incomeStr);
			}
			member.favFilmType = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_FILMTYPES, 30);
			String fqCinemaDistStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_FQCINEMADIST, 8);
			if (!"".equals(fqCinemaDistStr)) {
				member.fqCinemaDist = Integer.parseInt(fqCinemaDistStr);
			}
			String fqCinemaTimeStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_FQCINEMATIME, 8);
			if (!"".equals(fqCinemaTimeStr)) {
				member.fqCinemaTime = Integer.parseInt(fqCinemaTimeStr);
			}
			member.mobileOptin = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_MOBILEOPTION, 50);
			cinemaCode = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_COUNTER, 10);// 注册影影城
			member.operator = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OPERATOR, 50);
			member.operatorName = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OPERATORNAME, 50);
			// 招募渠道
			member.registChnID = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_SOURCEFOR, 11);
			
			try {
				//招募渠道扩展
				member.registChnExtID = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_SOURCEFOR_EXT, 11);
			} catch (Exception e) {
				member.registChnExtID ="POS";
			}
			// 会员来源
			member.dtsId = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_DTSID, 11);
			String istalkStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_ISTALK, 8);
			if (!"".equals(istalkStr)) {
				member.istalk = Integer.parseInt(istalkStr);
			}
			member.otherNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OTHERNO, 50);
			favCinemaCode = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_FAV_CINEMA, 10);
			String registDate = getChildValueByName(root,ConstDef.CONST_INTFCODE_M_REGISTER_DATE, 20);
			member.registDate = "".equals(registDate)?FormatTools.getNowTime():registDate;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer buf = new StringBuffer();
		buf.append(createXmlTag("MEMBER_NO", member.memberNo));
		buf.append(createXmlTag("SALES_TALK_XML",member.talk));
		buf.append(createXmlTag("LEVEL_POINT_TOTAL", "" + member.levelPointTotal));// 定级积分
		buf.append(createXmlTag("ACTIVITY_POINT", "" + member.activityPoint));// 非定级积分
		buf.append(createXmlTag("EXG_POINT_BALANCE", "" + member.exgPointBalance));// 可消费积分
		buf.append(createXmlTag("LEVEL_CODE", member.levelCode));// 会员级别编码
		buf.append(createXmlTag("EXPIRE_DATE", member.expireDate));// 会员级别有效期
		buf.append(createXmlTag("ORG_LEVEL", member.orgLevel));// 会员上次级别编码
		buf.append(createXmlTag("SET_TIME", member.setTime));// 级别变更时间
		buf.append(createXmlTag("TARGET_LEVEL", member.targetLevel));// 期望级别编码
		buf.append(createXmlTag("LEVEL_POINT_OFFSET", member.levelPointOffset));// 期望级别差距积分
		return buf.toString();
	}
}