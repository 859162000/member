package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.bean.MemberCardRelBean;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

public class QueryCarList  extends ServiceBase{
	private List<MemberCardRelBean> list;
	public QueryCarList(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_QUERYCARDLIST;
	}
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		this.checkMember(conn);//验证会员是否存在
		list=new ArrayList<MemberCardRelBean>();
		
		//查询关联卡
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_CARD_REL, this.memberNo);
		ResultSet rs=rsq.getResultSet();
		while (rs.next()) {
			MemberCardRelBean bean=new MemberCardRelBean();
			bean.setCardNo(rs.getString("CARD_NUMBER"));
			bean.setCardTypeName(rs.getString("TYPE_NAME"));
			bean.setCardTypeCode(rs.getString("TYPE_CODE"));
			bean.setCardType("1");
			list.add(bean);
		}
		rsq.free();//释放对象
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root,"MEMBER_NO", 64);
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer sb=new StringBuffer();
		for (MemberCardRelBean bean : this.list) {
			sb.append("<CARD_INFO>");
			sb.append(this.createXmlTag("CARD_NO", bean.getCardNo()));
			sb.append(this.createXmlTag("CARDTYPE_NAME", bean.getCardTypeName()));
			sb.append(this.createXmlTag("CARDTYPE_CODE", bean.getCardTypeCode()));
			sb.append(this.createXmlTag("CARD_TYPE", bean.getCardType()));
			sb.append("</CARD_INFO>");
		}
		return sb.toString();
	}

}