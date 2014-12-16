package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

public class RedeemResult extends ServiceBase {
	private String orderNo;
	private String memberStatus;
	private String cinemaInnerCode = "";
	private String cinema;

	public RedeemResult() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_REDEEMRESULT;
		this.timeOutFlag = true;
	}

	@Override
	protected void bizPerform() throws Exception {
		String productName = "";
		Connection conn = getDBConnection();
		int mid = this.checkMember(conn);// 验证会员是否存在

		// 查询影城内码
		ResultQuery rsq = SqlHelp.query(conn,
				SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE, cinema);
		ResultSet rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			cinemaInnerCode = rs.getString("inner_code");
		} else {
			throwsBizException("M070004", "影城编码错误！");
		}
		rsq.free();

		// 验证订单号存在
		rsq = SqlHelp.query(conn, SQLConstDef.CHECK_TRANS_ORDER, this.orderNo,
				cinemaInnerCode, "1", String.valueOf(mid));
		rs = rsq.getResultSet();
		double exPoint = 0;
		double myBalance = 0;// 会员可消费积分
		int phid = 0;
		if (rs != null && rs.next()) {
			phid = rs.getInt("POINT_HISTORY_ID");
			rsq = SqlHelp.query(conn, SQLConstDef.SELECT_MEMBER_EXCHANGE_POINT,
					String.valueOf(phid));
			rs = rsq.getResultSet();
			if (rs != null && rs.next()) {
				exPoint = -1 * rs.getDouble("EXCHANGE_POINT");
				productName = rs.getString("PRODUCT_NAME");
			}
		} else {
			throwsBizException("M080001", "订单不存在或已回退!");
		}
		rsq.free();

		// 会员状态是否被禁用
		rsq = SqlHelp.query(conn, SQLConstDef.MEMBER_CHECK_BY_MEMBERNO,
				memberNo);
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			memberStatus = rs.getString("STATUS");
			if (!"1".equals(memberStatus)) {
				throwsBizException("M080003", "会员已禁用！");
			}
		} else {
			throwsBizException("M080002", "会员不存在！");
		}
		rsq.free();

		// 查询出会员的可用积分余额
		rsq = SqlHelp.query(conn, SQLConstDef.QUERY_POINT_BALANCE,
				this.memberNo);
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			myBalance = rs.getDouble("EXG_POINT_BALANCE");
		} else {
			throwsBizException("M080002", "会员不存在,或可无可用积分");
		}
		SqlHelp.operate(conn, SQLConstDef.UPDATE_POINT_HISTORY, "0",
				String.valueOf(phid));
		SqlHelp.operate(conn, SQLConstDef.INSERT_POINT_HISTORY, 
				"6", //1 POINT_TYPE
				"1", //2 VERSION
				String.valueOf(mid), //3 MEMBER_ID
				"1",  //4 POINT_SYS
				String.valueOf(myBalance),//5 ORG_POINT_BALANCE 变化前
				"0", //6 LEVEL_POINT
				"0", //7 ACTIVITY_POINT
				"0", //8 IS_SYNC_BALANCE
				String.valueOf(myBalance + Double.valueOf(exPoint)),// 变化后  9 POINT_BALANCE
				"0",   //变化额   10 ISDELETE
				String.valueOf(exPoint), //11 EXCHANGE_POINT
				productName,  //12 PRODUCT_NAME
				orderNo,  //13 ORDER_ID
				"2",  //14 POINT_TRANS_TYPE
				"0",  //15 IS_SUCCEED  1成功
				"T",   //16 CREATE_BY
				cinemaInnerCode,  //17 CINEMA_INNER_CODE
				orderNo); //18 POINT_TRANS_CODE
		
		// 积分回滚
		SqlHelp.operate(conn, SQLConstDef.UPDATE_POINT_BALANCE,
				String.valueOf(Double.valueOf(exPoint)), String.valueOf(mid));
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root, "MEMBER_NO", 64);
		orderNo = getChildValueByName(root, "ORDER_NO", 50);
		cinema = getChildValueByName(root, "CINEMA", 8);
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}

}