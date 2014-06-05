package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;


/**
 *	会员最近一次影票消费计算 
 * @author wangshuai
 * @date 2013-07-14	
 */


public class Resultposorder implements LineHandle{
	private Connection conn;
	public Resultposorder(Connection conn){
		this.conn=conn;
	}
	public Resultposorder(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;
		
		 
		String sqlfandmember = "select TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,MEMBER_NUM,IS_POINT,CINEMA_INNER_CODE,MEMBER_ID,FILM_CODE,FILM_NAME,HALL_NUM,SHOW_TIME,BIZ_DATE,TRANS_TIME,POINT,POINT_AMOUNT,TRANS_ORDER_ID,HIS_MEMBER_NO from T_TICKET_TRANS_ORDER where TRANS_ORDER_ID=?";
		
		String sql="select MEMBER_ID,LAST_TICKET,LAST_TICKET_SHOW_TIME,LAST_TICKET_PAY,LAST_CON_ORDER,LAST_CON_PAY,LAST_TICKET_ORDER,LAST_CON_TRANS_TIME,LAST_TRANS_ORDER,LAST_TICKET_FILM,LAST_CON_CINEMA,LAST_CON_ID,LAST_TRANS_CINEMA,LAST_TRANS_TIME,LAST_TRANS_TYPE,LAST_TICKET_CINEMA from T_MBR_TAG_RESULT where MEMBER_ID=? ";
		
		
	//	String sqlupmember="UPDATE T_MBR_TAG_RESULT SET LAST_TICKET=?,LAST_TICKET_SHOW_TIME=?,LAST_TICKET_PAY=?,LAST_TICKET_ORDER=?,LAST_TICKET_FILM=?,LAST_TICKET_CINEMA=?  where MEMBER_ID=?";
		
		String sqlupmember="UPDATE T_MBR_TAG_RESULT SET LAST_TICKET=?,LAST_TICKET_PAY=?,LAST_TICKET_SHOW_TIME=to_date(?, 'yyyy-mm-dd hh24:mi:ss'),LAST_TICKET_ORDER=?,LAST_TICKET_FILM=?,LAST_TICKET_CINEMA=?,LAST_TRANS_TYPE=?  where MEMBER_ID=?";
		
		String sqlin="insert into T_MBR_TAG_RESULT (MEMBER_ID,LAST_TICKET,LAST_TICKET_PAY,LAST_TICKET_SHOW_TIME,LAST_TICKET_ORDER,LAST_TICKET_FILM,LAST_TICKET_CINEMA,LAST_TRANS_TYPE)values(?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
		 	                    
		String sqlhash="select T_TICKET_PAYMENT_ID,PAYMENT_NAME,PAYMENT_HASH from T_TICKET_PAYMENT_TYPE where PAYMENT_HASH=?";
		
		
		 
		
				
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field hashfield=fieldset.getFieldByName("PAYMENT_HASH");
		Field lpfield=fieldset.getFieldByName("TRANS_ORDER_ID");//1,2
		Field irield=fieldset.getFieldByName("IS_RESULT");
		Field oidfield=fieldset.getFieldByName("ORDER_ID");
 
		Field gidfield=fieldset.getFieldByName("TICKET_NO"); //票号
		
 //
		ResultQuery rq= SqlHelp.query(conn, sqlfandmember,lpfield.srcValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		 
		try {
			if(rs!=null&&rs.next()){
				String ha = hashfield.destValue;
				ResultQuery harq= SqlHelp.query(conn, sqlhash,ha);
				ResultSet hars= harq.getResultSet();
			String pay ="";
				if(hars!=null&&hars.next()){
					pay =hars.getString("PAYMENT_NAME");
				}
				harq.free();
			//	mfield.destValue=
				
				ResultQuery grrq= SqlHelp.query(conn, sql,mfield.srcValue);
				ResultSet grrs= grrq.getResultSet();
				Date rsshowtime=rs.getTimestamp("SHOW_TIME");
				
				String aa = DateUtil.getDateStrss(rs.getTimestamp("SHOW_TIME"));
				String code =	rs.getString("CINEMA_INNER_CODE");
				 String fc= rs.getString("FILM_CODE");
				if(grrs!=null&&grrs.next()){
					
				//Date showtime =	grrs.getDate("LAST_TICKET_SHOW_TIME");
					Date showtime =	grrs.getTimestamp("LAST_TICKET_SHOW_TIME");
					
					//sqlhash 
			
				String ss = DateUtil.getDateStrss(showtime);
				if(showtime!=null){
				if(showtime.before(rsshowtime)){//对比时间更新最近消费表
					

				//	System.out.println("最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
				//	System.out.println("影城订单表T_TICKET_TRANS_ORDER-SHOW_TIME"+aa);
					//date.before() 判断时间大小
					
					
					
				 
					 String conorder=	grrs.getString("LAST_CON_ORDER");
					 if(oidfield.destValue.equals(conorder)&&conorder!=null){
						 String lasttype = "all";
						 String[] names = {gidfield.destValue,pay,aa,oidfield.destValue,fc,code,lasttype,mfield.destValue};
							
						 SqlHelp.operate(conn, sqlupmember, names);
					
					 }else{
						 String lasttype = "ticket";
					//	 String[] names1 = {"最近观看的影票","最近观看影票的支付方式","最近观看影票的放映时间","最近观看影票对应的订单号","最近观看影票对应影片seqid","最近观看影票对应的影城内码innercode"};
							
						 String[] names = {gidfield.destValue,pay,aa,oidfield.destValue,fc,code,lasttype,mfield.destValue};
							
					int j =	 SqlHelp.operate(conn, sqlupmember, names);
						// System.out.println("j="+j);
					 }
					
				
				}else{

					
					
				//	System.out.println("else最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
				//	System.out.println("else影城订单表T_TICKET_TRANS_ORDER-SHOW_TIME"+aa);
					
				}
				}else{
					 String lasttype = "ticket";
					 String[] names = {gidfield.destValue,pay,aa,oidfield.destValue,fc,code,lasttype,mfield.destValue};
						
						int j =	 SqlHelp.operate(conn, sqlupmember, names);
					
					
				}
					
					
				}else{
					
					//插入最近消费表一条新的字段
					
 
					

					String[] ddnames1 = {"会员ID","最近观看的影票","最近观看影票的支付方式","最近观看影票的放映时间","最近观看影票对应的订单号","最近观看影票对应影片seqid","最近观看影票对应的影城内码innercode"};
						 	
					String[] ddnames = {mfield.destValue,gidfield.destValue,pay,aa,oidfield.destValue,fc,code,"ticket"};
					
				int a =	SqlHelp.operate(conn, sqlin, ddnames);
				//	System.out.println(a);
				}

				String is ="1";
				
 
				 
				irield.destValue=is;
				grrq.free();
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
