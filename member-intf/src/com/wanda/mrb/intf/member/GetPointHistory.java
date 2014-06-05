package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.bean.MemberPointHistoryBean;
import com.wanda.mrb.intf.member.vo.TMember;

public class GetPointHistory  extends ServiceBase{
	private List<MemberPointHistoryBean> list;
	public GetPointHistory(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_GETPOINTSHISTORY;
	}
	
	TMember member = new TMember();
	String startDate = "";
	String endDate = "";
	
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		this.checkMember(conn,member.memberNo);//验证会员是否存在
		list=new ArrayList<MemberPointHistoryBean>();
		ps = conn.prepareStatement(SQLConstDef.QUERY_POINT_HISTORY);
		ps.setString(1, member.memberNo);
		ps.setString(2, startDate);
		ps.setString(3, endDate);
		rs = ps.executeQuery();
		while (rs.next()) {
			MemberPointHistoryBean bean=new MemberPointHistoryBean();
			bean.setOpTime(rs.getString("SET_TIME"));
			bean.setOpType(rs.getString("POINT_TYPE"));
			bean.setPointCount(rs.getString("EXCHANGE_POINT"));
			bean.setExchangeTime(rs.getString("EXCHANGE_POINT_EXPIRE_TIME"));
			bean.setProduct_name(rs.getString("PRODUCT_NAME"));
			list.add(bean);
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		member.memberNo = getChildValueByName(root,ConstDef.CONST_INTFCODE_M_REGISTER_MEMBERNO, 64);
		validateParamNotEmpty(member.memberNo, "会员编号");
		startDate = getChildValueByName(root,ConstDef.CONST_INTFCODE_M_GET_TRANS_HISTORY_START_DATE, 20);
		validateParamNotEmpty(startDate, "开始时间");
		endDate = getChildValueByName(root,ConstDef.CONST_INTFCODE_M_GET_TRANS_HISTORY_END_DATE, 20);
		validateParamNotEmpty(endDate, "结束时间");
		
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer sb=new StringBuffer();
		for (MemberPointHistoryBean bean : list) {
			sb.append("<POINT_INFO>");
			sb.append(this.createXmlTag("OPTIME", bean.getOpTime()));
			sb.append(this.createXmlTag("OPTYPE", bean.getOpType()));
			sb.append(this.createXmlTag("POINT_COUNT", bean.getPointCount()));
			sb.append(this.createXmlTag("EXCHANGE_TIME", bean.getExchangeTime()));
			sb.append(this.createXmlTag("PRODUCT_NAME", bean.getProduct_name()));
			sb.append("</POINT_INFO>");
		}
		return sb.toString();
	}

}