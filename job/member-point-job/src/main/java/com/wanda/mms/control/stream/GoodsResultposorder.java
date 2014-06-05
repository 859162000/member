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
 *	会员最近一次卖品消费计算 
 * @author wangshuai
 * @date 2013-07-14	
 */


public class GoodsResultposorder implements LineHandle{
	private Connection conn;
	public GoodsResultposorder(Connection conn){
		this.conn=conn;
	}
	public GoodsResultposorder(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 
		int r=0;

		MemberPoint mb = new MemberPoint();
		String sqlfandmember = "select GOODS_TRANS_ID,ORDER_ID,TOTAL_AMOUNT,MEMBER_NUM,IS_POINT,TRANS_TYPE,CINEMA_INNER_CODE,MEMBER_ID,GOOD_NAME,BIZ_DATE,TRANS_TIME,POINT,TRANS_ORDER_ID from T_GOODS_TRANS_ORDER where TRANS_ORDER_ID=?";
		
		String sql="select MEMBER_ID,LAST_TICKET,LAST_TICKET_SHOW_TIME,LAST_TICKET_PAY,LAST_CON_ORDER,LAST_CON_PAY,LAST_TICKET_ORDER,LAST_CON_TRANS_TIME,LAST_TRANS_ORDER,LAST_TICKET_FILM,LAST_CON_CINEMA,LAST_CON_ID,LAST_TRANS_CINEMA,LAST_TRANS_TIME,LAST_TRANS_TYPE,LAST_TICKET_CINEMA from T_MBR_TAG_RESULT where MEMBER_ID=? ";
		
	//	String sqlupmember="UPDATE T_MBR_TAG_RESULT SET LAST_TICKET=?,LAST_TICKET_SHOW_TIME=?,LAST_TICKET_PAY=?,LAST_TICKET_ORDER=?,LAST_TICKET_FILM=?,LAST_TICKET_CINEMA=?  where MEMBER_ID=?";
		
		String sqlupmember="UPDATE T_MBR_TAG_RESULT SET LAST_CON_ORDER=?,LAST_CON_PAY=?,LAST_CON_TRANS_TIME=to_date(?, 'yyyy-mm-dd hh24:mi:ss'),LAST_CON_CINEMA=?,LAST_CON_ID=?,LAST_TRANS_TYPE=?  where MEMBER_ID=?";
		
		String sqlin="insert into T_MBR_TAG_RESULT(MEMBER_ID,LAST_CON_ORDER,LAST_CON_PAY,LAST_CON_TRANS_TIME,LAST_CON_CINEMA,LAST_CON_ID,LAST_TRANS_TYPE)values(?,?,?,to_date(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
		                      
		String sqlhash="";
		
		
		 
		
				
		Field mfield=fieldset.getFieldByName("MEMBER_ID");
		Field hashfield=fieldset.getFieldByName("PAYMENT_HASH");
		Field lpfield=fieldset.getFieldByName("POS_ORDER_ID");//1,2
		Field irield=fieldset.getFieldByName("IS_RESULT");
		Field oidfield=fieldset.getFieldByName("ORDER_ID");
		Field oridfield=fieldset.getFieldByName("POINT_TRANS_CODE"); //2 
		Field gidfield=fieldset.getFieldByName("GOODS_ID"); //品项ID
		Field codefield=fieldset.getFieldByName("CINEMA_INNER_CODE"); //品项ID
		Field timefield=fieldset.getFieldByName("TRANS_TIME"); //品项ID
		//
	//	System.out.println(mfield.destValue);
		Date rsshowtime = DateUtil.getStringForDate(timefield.destValue)  ;
 //
		ResultQuery rq= SqlHelp.query(conn, sql,mfield.srcValue);
		ResultSet rs= rq.getResultSet();
		int i =0;
		 
		try {
			if(rs!=null&&rs.next()){
			//	mfield.destValue=
				String aa = DateUtil.getDateStrss(rsshowtime);	
				//Date showtime =	grrs.getDate("LAST_TICKET_SHOW_TIME");
				Date showtime =	rs.getTimestamp("LAST_CON_TRANS_TIME");
				
				if(showtime!=null){
					String ss = DateUtil.getDateStrss(showtime);
				if(showtime.before(rsshowtime)){//对比时间更新最近消费表

				//	System.out.println("最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
				//	System.out.println("卖品订单表T_GOODS_TRANS_ORDER-TRANS_TIME"+timefield.destValue);

					String ticketorder=	rs.getString("LAST_TICKET_ORDER");
					 if(oidfield.destValue.equals(ticketorder)){
						 String lasttype = "all";
						//	System.out.println("最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
						//	System.out.println("卖品订单表T_GOODS_TRANS_ORDER-TRANS_TIME"+timefield.destValue);
						
						 String[] names1 = {"最近购买品项的订单号","最近购买卖品ID对应的交易方式","最近购买品项的交易时间","最近购买卖品ID对应的影城","最近购买品项ID"};
							String[] names = {oidfield.destValue,hashfield.destValue,timefield.destValue,codefield.destValue,gidfield.destValue,lasttype,mfield.destValue};
							
							SqlHelp.operate(conn, sqlupmember, names);
						
					 }else{
						 String lasttype = "con";
						//	System.out.println("最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
						//	System.out.println("卖品订单表T_GOODS_TRANS_ORDER-TRANS_TIME"+timefield.destValue);
							//date.before() 判断时间大小 
							 
						    
							String[] names1 = {"最近购买品项的订单号","最近购买卖品ID对应的交易方式","最近购买品项的交易时间","最近购买卖品ID对应的影城","最近购买品项ID"};
							String[] names = {oidfield.destValue,hashfield.destValue,timefield.destValue,codefield.destValue,gidfield.destValue,lasttype,mfield.destValue};
							
							SqlHelp.operate(conn, sqlupmember, names);
						 
					 }

				
				}else{
					
				//	System.out.println("else最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME"+ss);
				//	System.out.println("else卖品订单表T_GOODS_TRANS_ORDER-TRANS_TIME"+timefield.destValue);

					}
				}else{
					 String lasttype = "con";
					//	System.out.println("最新影城消费表。T_MBR_TAG_RESULT-LAST_CON_TRANS_TIME");
					//	System.out.println("卖品订单表T_GOODS_TRANS_ORDER-TRANS_TIME"+timefield.destValue);
						//date.before() 判断时间大小 
						 
					    
						String[] names1 = {"最近购买品项的订单号","最近购买卖品ID对应的交易方式","最近购买品项的交易时间","最近购买卖品ID对应的影城","最近购买品项ID"};
						String[] names = {oidfield.destValue,hashfield.destValue,timefield.destValue,codefield.destValue,gidfield.destValue,lasttype,mfield.destValue};
						
						SqlHelp.operate(conn, sqlupmember, names);
					
				}
					
			 
					
			 

				String is ="1";
				
 
				 
				irield.destValue=is;
	
				r=1;
			}else{
				
				String lasttype = "con";

				String[] ddnames1 = {"会员ID","最近购买品项的订单号","最近购买卖品ID对应的交易方式","最近购买品项的交易时间","最近购买卖品ID对应的影城","最近购买品项ID"};
					
				String[] ddnames = {mfield.destValue,oidfield.destValue,hashfield.destValue,timefield.destValue ,codefield.destValue ,gidfield.destValue,lasttype};
				
				SqlHelp.operate(conn, sqlin, ddnames);
				String is ="1";
				irield.destValue=is;
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
