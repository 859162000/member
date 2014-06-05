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
import com.wanda.mms.control.stream.util.DateUtil;

/**
 *	会员积分计算 ：影票计算
 * @author wangshuai
 * @date 2013-07-09	
 */


public class PointLineHandle implements LineHandle{
	private Connection conn;
	public PointLineHandle(Connection conn){
		this.conn=conn;
	}
	public PointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		Dimdef dd = new Dimdef();
		//String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where to_char(BIZ_DATE,'yyyymmdd') between ? and ?  group by MEMBER_ID ";
	//	String sql ="select  MEMBER_ID from T_TICKET_TRANS_DETAIL t   where IS_POINT!=? and IS_POINT!=? and IS_POINT!=?  group by MEMBER_ID ";
		String sqlFilm ="SELECT A.*, ROWNUM RN  FROM ( select   TICKET_TRANS_DETAIL_ID,TICKET_TYPE_NAME,TICKET_TYPE_CODE,AMOUNT,PAYMENT_HASH,TICKET_NO,TRANS_ORDER_ID "
 +" ,ORDER_ID,CINEMA_INNER_CODE,BIZ_DATE,POINT,MEMBER_ID,HIS_MEMBER_NO ,LEVEL_POINT,ACTIVITY_POINT,IS_POINT,IS_TICKET "
 +" from T_TICKET_TRANS_DETAIL t  where MEMBER_ID =? and (IS_POINT ='2' or IS_POINT ='3' or IS_POINT ='4' )  and BIZ_DATE = to_date(?, 'yyyy-mm-dd') and CINEMA_INNER_CODE=?  order by point desc  ,LEVEL_POINT desc ,ACTIVITY_POINT desc ) A WHERE ROWNUM <=4";
		 
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
		String sqlup="UPDATE T_TICKET_TRANS_DETAIL SET IS_TICKET=? WHERE TICKET_TRANS_DETAIL_ID=?";
	//	String sqll = "select * from T_DIMDEF where typeid=? and name=?";
		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field idield=fieldset.getFieldByName("TICKET_TRANS_DETAIL_ID");
		Field cicfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
//		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
//		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");
		//Field ispfield=fieldset.getFieldByName("IS_POINT");
		Field bzfield=fieldset.getFieldByName("BIZ_DATE");
		Field itpfield=fieldset.getFieldByName("IS_TICKET");
		//Field nfield=fieldset.getFieldByName("MEMBER_NUM");
		//Field filmFiled=fieldset.getFieldByName("FILM_NAME");  ?
		
		Field filmFiled=null;
		String bz = DateUtil.getDateStrymd((DateUtil.getStringForDate(bzfield.destValue)));
		
		//System.out.println(mfield.destValue);
		//System.out.println(sqlFilm);
		ResultQuery rq= SqlHelp.query(conn, sqlFilm,mfield.destValue,bz,cicfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		try {
			while(rs.next()){
				itpfield.destValue="1";
				 String ss =rs.getString("TICKET_TRANS_DETAIL_ID");
				 String point = rs.getString("POINT");
				 String level_point = rs.getString("LEVEL_POINT");
				 String activity_point = rs.getString("ACTIVITY_POINT");
			//	FieldSet fi = new FieldSet();
			//	fi.fields.add(itpfield);
			//	fi.fields.add(idield);
			//	System.out.println("UPDATE T_TICKET_TRANS_DETAIL SET IS_TICKET=? WHERE TICKET_TRANS_DETAIL_ID=? ");
				 String[] names = {"1",ss};
			    SqlHelp.operate(conn, sqlup,names);
			//	 System.out.println("计算影票ID号"+ss+"总积分"+point+"定级积分"+level_point+"非定级积分"+activity_point);
				r=0;
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
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
