package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;

/**
 *	会员积分计算 ：影票计算
 * @author wangshuai
 * @date 2013-07-09	
 */


public class MemberPointLineHandle implements LineHandle{
	private Connection conn;
	public MemberPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public MemberPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 //1
		int r=0;
		Dimdef dd = new Dimdef();
		String sql ="select SEGMENT_ID,NAME,CODE,SEGMENT_TYPE,CRITERIA_SCHEME,SORT_NAME,SORT_ORDER,MAX_COUNT,CAL_COUNT,UPDATE_BY,UPDATE_DATE from T_SEGMENT t where SEGMENT_ID=?";
		// String sqlFilm =" ";
		 
	//	String sqlType ="select t_ticket_payment_id,payment_name,payment_hash from T_TICKET_PAYMENT_TYPE t where PAYMENT_HASH=?";//查询出支付方式
		String sqlup="UPDATE T_TICKET_TRANS_DETAIL SET IS_TICKET=? WHERE TICKET_TRANS_DETAIL_ID=?";
		String sqll = "select * from T_DIMDEF where typeid=? and name=?";
		
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field idfield=fieldset.getFieldByName("SEGMENT_ID");
 
		Field itpfield=fieldset.getFieldByName("IS_TICKET");
 
		Field filmFiled=null;

		//System.out.println(mfield.destValue);
		 
		ResultQuery rq= SqlHelp.query(conn, sql,idfield.destValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		try {
			if(rs!=null&&rs.next()){
				String segment_type=rs.getString("SEGMENT_TYPE");
				if(segment_type.equals("member")){
					//解析  
					
				}
			}
			while(rs.next()){
				itpfield.destValue="1";
			 
				FieldSet fi = new FieldSet();
				fi.fields.add(itpfield);
			 
			int a = SqlHelp.operate(conn, sqlup,fi);
				//System.out.println(a); 
			
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
