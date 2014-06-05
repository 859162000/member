package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;


/**
 *	会员积分计算 ：卖品计算
 * @author wangshuai
 * @date 2013-07-09	
 */

public class GoodsPointLineHandle implements LineHandle{
	private Connection conn;
	public GoodsPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public GoodsPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		Dimdef dd = new Dimdef();
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		String sqlFilm =" select   T_GOODS_TRANS_DETAIL_ID,GOODS_NAME ,AMONT   ,TRANS_TYPE,TRANS_TIME,GODDS_SUD,GOODS_COUNT,POS_ORDER_ID,ORDER_ID "
		 +" ,BIZ_DATE,POINT,PAYMENT_HASH ,LEVEL_POINT  ,ACTIVITY_POINT,IS_POINT,IS_goods ,EXT_POINT_RULE_ID,MEMBER_ID,GOODS_ID,CINEMA_INNER_CODE "
		  +" from T_GOODS_TRANS_DETAIL t  where MEMBER_ID =? and IS_POINT=2  and CINEMA_INNER_CODE=?";
		 
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
		String sqlup="UPDATE T_GOODS_TRANS_DETAIL SET IS_GOODS=? WHERE T_GOODS_TRANS_DETAIL_ID=?";
		 
		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field idfield=fieldset.getFieldByName("T_GOODS_TRANS_DETAIL_ID");
		Field cicfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
//		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
//		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");
		//Field ispfield=fieldset.getFieldByName("IS_POINT");
//		Field pfield=fieldset.getFieldByName("POINT");
		Field itpfield=fieldset.getFieldByName("IS_GOODS");
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		Field filmFiled=null;

	//	System.out.println(mfield.destValue);
	//	System.out.println(sqlFilm);
		ResultQuery rq= SqlHelp.query(conn, sqlFilm,mfield.destValue,cicfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		try {
			while(rs.next()){
				String ig="1";
				String id =  rs.getString("T_GOODS_TRANS_DETAIL_ID");
				 
				 String[] ddnames = {ig,id};
			int a = SqlHelp.operate(conn, sqlup,ddnames);
			//	System.out.println(a); 
			
				r=1;
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			rq.free();
		}
		return r;
	}
	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	
}
