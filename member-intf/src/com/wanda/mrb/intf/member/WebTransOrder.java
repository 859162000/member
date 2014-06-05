/**
 * 
 */
package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

/**
 * @author xuesi
 *
 */
public class WebTransOrder extends ServiceBase{
	private String memberNo = "";
	private String cinemaCode = "";
	private String posOrderNo = "";
	private String webOrderNo = "";
	public WebTransOrder() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_WEBTRANSORDER;
	}
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		//查询影城内码
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.SELECT_WEB_ORDER_NO, webOrderNo);
		ResultSet rs=rsq.getResultSet();
		if(rs == null || !rs.next()){
			throwsBizException("M160001", "网站订单不存在或已回退!");
		}
		rsq.free();
		//回写网站传的POS订单号
		SqlHelp.operate(conn, SQLConstDef.UPDATE_WEB_ORDER_NO, 
				posOrderNo,
				posOrderNo,
				webOrderNo);
		SqlHelp.operate(conn, SQLConstDef.UPDATE_WEB_TICKET_ORDER_NO, 
				posOrderNo,
				webOrderNo);
		SqlHelp.operate(conn, SQLConstDef.UPDATE_WEB_GOODS_ORDER_NO, 
				posOrderNo,
				webOrderNo);
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		memberNo = getChildValueByName(root,"MEMBER_NO", 64);
		webOrderNo = getChildValueByName(root,"WEB_ORDER_NO", 50);
		posOrderNo = getChildValueByName(root,"POS_ORDER_NO", 50);
		cinemaCode = getChildValueByName(root,"CINEMA", 8);		
	}

	@Override
	protected String composeXMLBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
