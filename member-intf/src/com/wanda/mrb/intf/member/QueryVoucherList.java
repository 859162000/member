package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.bean.MemberVoucherRelBean;
import com.wanda.mrb.intf.utils.FormatTools;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.VoucherCodeUtil;

public class QueryVoucherList  extends ServiceBase{
	private List<MemberVoucherRelBean> list;
	public QueryVoucherList(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_QUERYVOUCHERLIST;
	}
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		this.checkMember(conn);//验证会员是否存在
		list=new ArrayList<MemberVoucherRelBean>();
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_VOUCHER_REL, this.memberNo,"A");
		ResultSet rs=rsq.getResultSet();
		while (rs!=null && rs.next()) {
			MemberVoucherRelBean bean=new MemberVoucherRelBean();
			bean.setVoucherNumber(VoucherCodeUtil.desDecrypt(rs.getString("PRINT_CODE")));
			bean.setVoucherTypeCode(rs.getString("TYPE_CODE"));
			bean.setVoucherTypeCodeName(rs.getString("TYPE_NAME"));
			bean.setStartDate(FormatTools.formatDateSS(rs.getString("SALE_DATE")));
			bean.setEndDate(FormatTools.formatDateSS(rs.getString("EXPIRY_DATE")));
			bean.setUseType(rs.getString("USE_TYPE"));
			bean.setOperateType(rs.getString("OPERRATE_TYPE"));
			bean.setUnitValue(rs.getString("UNIT_VALUE"));
			bean.setMimPrice(rs.getString("MIN_PRICE"));
			list.add(bean);
		}
		rsq.free();
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root,"MEMBER_NO", 64);
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer sb=new StringBuffer();
		for (MemberVoucherRelBean bean : this.list) {
			sb.append("<VOUCHER_INFO>");
			sb.append(this.createXmlTag("VOUCHER_NUMBER", bean.getVoucherNumber()));
			sb.append(this.createXmlTag("VOUCHERTYPE_CODE", bean.getVoucherTypeCode()));
			sb.append(this.createXmlTag("VOUCHERTYPE_NAME", bean.getVoucherTypeCodeName()));
			sb.append(this.createXmlTag("START_TIME", bean.getStartDate()));
			sb.append(this.createXmlTag("END_TIME", bean.getEndDate()));
			sb.append(this.createXmlTag("USE_TYPE", bean.getUseType()));
			sb.append(this.createXmlTag("OPERRATE_TYPE", bean.getOperateType()));
			sb.append(this.createXmlTag("SHOW_VALUE", bean.getUnitValue()));
			sb.append(this.createXmlTag("MIN_PRICE", bean.getMimPrice()));
			sb.append("</VOUCHER_INFO>");
		}
		return sb.toString();
	}

}