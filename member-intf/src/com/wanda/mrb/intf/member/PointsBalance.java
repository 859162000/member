package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;

public class PointsBalance extends ServiceBase{
	private String balance;
	public PointsBalance(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_GETPOINTSBALANCE;
	}
	@Override
	/**
	 * 业务处理
	 */
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		this.checkMember(conn);//验证会员是否存在
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(SQLConstDef.QUERY_POINT_BALANCE);
		ps.setString(1, this.memberNo);
		rs = ps.executeQuery();
		if(rs !=null && rs.next()){
			balance=rs.getString("EXG_POINT_BALANCE");
		}else{
			throwsBizException("M060001", "会员积分账户异常");
		}
	}

	@Override
	/**
	 * 解析参数
	 */
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root,"MEMBER_NO", 64);
	}

	@Override
	/**
	 * 
	 */
	protected String composeXMLBody() {
		return createXmlTag("BALANCE",balance);
	}
}
