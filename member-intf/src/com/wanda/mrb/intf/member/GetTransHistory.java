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
import com.wanda.mrb.intf.bean.MemberTransHistoryBean;
import com.wanda.mrb.intf.member.vo.TMember;

public class GetTransHistory  extends ServiceBase{
	private List<MemberTransHistoryBean> list;
	public GetTransHistory() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_GETTRANSHISTORY;
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
		list=new ArrayList<MemberTransHistoryBean>();
		//买票
		ps = conn.prepareStatement(SQLConstDef.QUERY_TRANS_HISTORY_FILM);
		ps.setString(1, member.memberNo);
		ps.setString(2, startDate);
		ps.setString(3, endDate);
		ps.setString(4, member.memberNo);
		ps.setString(5, startDate);
		ps.setString(6, endDate);
		rs = ps.executeQuery();
		while (rs.next()) {
			MemberTransHistoryBean bean=new MemberTransHistoryBean();
			bean.setOrderNO(rs.getString("ORDER_ID"));
			bean.setPurTime(rs.getString("TRANS_TIME"));
			bean.setCinemaCode(rs.getString("CODE"));
			bean.setCinemaName(rs.getString("SHORT_NAME"));
			bean.setPrdType(rs.getString("PRDTYPE"));
			bean.setPrdName(rs.getString("HALL_NUM")+"号厅; 时间:"+rs.getString("SHOW_TIME")+"; 影片:"+rs.getString("FILM_NAME")+"; "+rs.getString("TICKET_NUM")+"张");
			bean.setAmount(rs.getString("TOTAL_AMOUNT"));
			bean.setIsPoint("1");
			bean.setPoint(rs.getString("POINT"));
			bean.setPointExchange(rs.getString("EXCHANGE_POINT"));
			list.add(bean);
		}
		//买卖品
		ps = conn.prepareStatement(SQLConstDef.QUERY_TRANS_HISTORY_GOOD);
		ps.setString(1, member.memberNo);
		ps.setString(2, startDate);
		ps.setString(3, endDate);
		ps.setString(4, member.memberNo);
		ps.setString(5, startDate);
		ps.setString(6, endDate);
		rs = ps.executeQuery();
		while (rs.next()) {
			MemberTransHistoryBean bean=new MemberTransHistoryBean();
			bean.setOrderNO(rs.getString("ORDER_ID"));
			bean.setPurTime(rs.getString("TRANS_TIME"));
			bean.setCinemaCode(rs.getString("CODE"));
			bean.setCinemaName(rs.getString("SHORT_NAME"));
			bean.setPrdType(rs.getString("PRDTYPE"));
			bean.setPrdName(rs.getString("GOOD_NAME"));
			bean.setAmount(rs.getString("TOTAL_AMOUNT"));
			bean.setIsPoint("1");
			bean.setPoint(rs.getString("POINT"));
			bean.setPointExchange(rs.getString("EXCHANGE_POINT"));
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
		for (MemberTransHistoryBean bean : list) {
			sb.append("<ORDER_INFO>");
			sb.append(this.createXmlTag("ORDER_NO", bean.getOrderNO()));
			sb.append(this.createXmlTag("PURTIME", bean.getPurTime()));
			sb.append(this.createXmlTag("CINEMA_CODE", bean.getCinemaCode()));
			sb.append(this.createXmlTag("CINEMA_NAME", bean.getCinemaName()));
			sb.append(this.createXmlTag("PRDTYPE", bean.getPrdType()));
			sb.append(this.createXmlTag("PRDNAME", bean.getPrdName()));
			sb.append(this.createXmlTag("AMOUNT", bean.getAmount()));
			sb.append(this.createXmlTag("ISPOINT", bean.getIsPoint()));
			sb.append(this.createXmlTag("POINT", bean.getPoint()));
			sb.append(this.createXmlTag("POINT_EXCHANGE", bean.getPointExchange()));
			sb.append("</ORDER_INFO>");
		}
		return sb.toString();
	}
}