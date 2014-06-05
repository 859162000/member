package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;




public class BasicPointLineHandle implements LineHandle{
	static Logger logger = Logger.getLogger(ActivityPointLineHandle.class.getName());
	final static String sqlFilm ="select   TICKET_TRANS_DETAIL_ID,TICKET_TYPE_NAME,TICKET_TYPE_CODE,AMOUNT,PAYMENT_HASH,TICKET_NO,TRANS_ORDER_ID "
			 +" ,ORDER_ID,CINEMA_INNER_CODE,BIZ_DATE,POINT,MEMBER_ID,HIS_MEMBER_NO ,LEVEL_POINT,ACTIVITY_POINT,IS_POINT,IS_TICKET "
			 +" from T_TICKET_TRANS_DETAIL t  where TICKET_TRANS_DETAIL_ID =? ";
	final static String sqll = "select * from T_DIMDEF where typeid=? and name=?";
	private Connection conn;
	public BasicPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public BasicPointLineHandle(){
		conn=Basic.mbr;
	}
	public static void  main(String[] args){
		System.out.println(sqlFilm);
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		Dimdef dd = new Dimdef();
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		
	 
		Field mfield=fieldset.getFieldByName("TICKET_TRANS_DETAIL_ID");
		Field amountfield=fieldset.getFieldByName("AMOUNT");
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");//定级积分
		Field ispfield=fieldset.getFieldByName("IS_POINT");//积分调度状态 积分调度状态 1 基本积分计算完成 2 特殊积分计算完成 3 插入积分历史等等 4 特殊积分计算
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?

//		System.out.println(mfield.destValue);
//		System.out.println(sqlFilm);
		ResultQuery rq= SqlHelp.query(conn, sqlFilm,mfield.destValue);//入参
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
					//System.out.println(code);
					
					double de = Double.valueOf(code);
					double am = Double.valueOf(amountfield.srcValue);
					double qjgz=am*de;
					String ss1 = String.valueOf(qjgz);
					BigDecimal d5 = new BigDecimal( ss1  );
					String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
				//	System.out.println(s6);
					logger.info("定级积分为："+s6);
					lpfield.destValue=s6;
					ispfield.destValue="1";
					logger.info("积分为处理："+ispfield.destValue);
//	
						
				}else{
				//	filmFiled.destValue="asdf";
				}
				rqdimdef.free();
				//rqtype.free();
			
				r=1;
			}		
		}catch (SQLException e) {
			logger.error(e);
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
