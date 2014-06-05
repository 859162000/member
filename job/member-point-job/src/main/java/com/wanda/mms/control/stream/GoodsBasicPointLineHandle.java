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



public class GoodsBasicPointLineHandle implements LineHandle{
	private Connection conn;
	public GoodsBasicPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public GoodsBasicPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		Dimdef dd = new Dimdef();
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		String sqlFilm ="select   T_GOODS_TRANS_DETAIL_ID,GOODS_NAME ,AMONT   ,TRANS_TYPE,TRANS_TIME,GODDS_SUD,GOODS_COUNT,POS_ORDER_ID,ORDER_ID "
	+"  ,BIZ_DATE,POINT,PAYMENT_HASH ,LEVEL_POINT  ,ACTIVITY_POINT,IS_POINT,IS_goods ,EXT_POINT_RULE_ID,MEMBER_ID,GOODS_ID "
 +" from T_GOODS_TRANS_DETAIL t  where T_GOODS_TRANS_DETAIL_ID =? ";
		String sqlType ="select t_GOODS_payment_id,payment_name,payment_hash from T_GOODS_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
	//	String sqlup="";
		String sqll = "select * from T_DIMDEF where typeid=? and name=?";
	 
		Field mfield=fieldset.getFieldByName("T_GOODS_TRANS_DETAIL_ID");
		Field amountfield=fieldset.getFieldByName("AMONT");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
		Field ispfield=fieldset.getFieldByName("IS_POINT");
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		Field filmFiled=null;

		//System.out.println(mfield.destValue);
	//	System.out.println(sqlFilm);
		ResultQuery rq= SqlHelp.query(conn, sqlFilm,mfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		try {
			if(rs!=null&&rs.next()){
			//	mfield.destValue=
				
				
				//ResultQuery rqtype= SqlHelp.query(conn,sqlType,rs.getString("PAYMENT_HASH"));
				//ResultSet rsfilm=rqtype.getResultSet();	
				ResultQuery rqdimdef= SqlHelp.query(conn,sqll,dd.POINT,dd.POINT_QJGZ);
				ResultSet rsdimdef=rqdimdef.getResultSet();	
				//if(rsfilm!=null&&rsfilm.next()&&rsdimdef.next()){
				if(rsdimdef!=null&&rsdimdef.next()){
					
					String code = rsdimdef.getString("code");
				//	System.out.println(code);
					
					double de = Double.valueOf(code);
					double am = Double.valueOf(amountfield.srcValue);
					double qjgz=am*de;
					String ss1 = String.valueOf(qjgz);
					BigDecimal d5 = new BigDecimal( ss1  );
					String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
				//	System.out.println(s6);
					lpfield.destValue=s6;
					ispfield.destValue="1";
//					 		
				}else{
				//	filmFiled.destValue="asdf";
				}
				rqdimdef.free();
				//rqtype.free();
			
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
