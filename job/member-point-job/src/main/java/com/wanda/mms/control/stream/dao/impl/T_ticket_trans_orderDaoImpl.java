package com.wanda.mms.control.stream.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_ticket_trans_orderDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.T_ticket_trans_order;

public class T_ticket_trans_orderDaoImpl implements T_ticket_trans_orderDao {
	static Logger logger = Logger.getLogger(T_ticket_trans_orderDaoImpl.class.getName());

	
	@Override
	public List<T_ticket_trans_order> fandT_ticket_trans_orderPage(
			Connection conn,int poit,Page page) {
		// TODO Auto-generated method stub
		 List<T_ticket_trans_order> ttlist = new ArrayList<T_ticket_trans_order>();
		 Date da = new Date();
		 String date = DateUtil.getDateStr(da);
			Calendar cal=Calendar.getInstance();
			int x=-2;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			
			String today=new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		 
		 Long seq = null  ;
//			String sql = "SELECT * FROM (  "
//					+ " SELECT A.*, ROWNUM RN  "
//					+ " FROM (select TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,MEMBER_NUM,IS_POINT,CINEMA_INNER_CODE,MEMBER_ID  " +
//							"from T_TICKET_TRANS_ORDER o where not exists(select set_time, order_id from" +
//							" T_POINT_HISTORY t where to_char(t.set_time,'yyyymmdd') between '"+today+"' and  '"+date+"' and  t.order_id=o.order_id   ) and  IS_POINT=?  "
//					+ "  　) A  "
//					+ " WHERE ROWNUM <= ?) "
//					+ " WHERE RN >= ? "; 
		 
			String sql = "SELECT * FROM (  "
				+ " SELECT A.*, ROWNUM RN  "
				+ " FROM (select TRANS_ID,ORDER_ID,TOTAL_AMOUNT,TICKET_NUM,MEMBER_NUM,IS_POINT,CINEMA_INNER_CODE,MEMBER_ID,POINT_AMOUNT  " +
						" from T_TICKET_TRANS_ORDER o where  IS_POINT=? and point_amount<>0 "
				+ "  　) A  "
				+ " WHERE ROWNUM <= ?) "
				+ " WHERE RN >= ? " ; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			
			logger.info(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
			ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

			try {
				pst=conn.prepareStatement(sql);
				pst.setInt(1,poit);
				pst.setLong(2,page.getPageSize());
				pst.setLong(3,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					T_ticket_trans_order tt= new T_ticket_trans_order();
					//会员交易表ID
					tt.setTrans_id(rs.getLong("TRANS_ID"));
					//POS 订单ID
					tt.setOrder_id(rs.getString("ORDER_ID"));
					//影票 金额 Math.round(f)
					
					//double f = 12.5;
					String ss = String.valueOf(rs.getDouble("TOTAL_AMOUNT"));
					BigDecimal d5 = new BigDecimal( ss  );
					String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
					//System.out.println(s6);
					long a = Long.valueOf(s6);
					
					tt.setTotal_amount(a);
					// 影票 数量
					tt.setTicket_num(rs.getInt("TICKET_NUM"));
					//会员手机号
					tt.setMember_num(rs.getString("MEMBER_NUM"));
					//是否计算 -- 状态
					tt.setIs_point(rs.getString("IS_POINT"));
					//积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
					tt.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
					
					tt.setMemberId(rs.getLong("MEMBER_ID"));
					//计算积分金额
					tt.setPoint_amount(rs.getLong("point_amount"));
					ttlist.add(tt);
				}
				 
				
		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				 
					logger.error(e);
				 
				e.printStackTrace();
			} finally {
				if(pst!=null){
					try {
						pst.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						logger.error(e);
						e.printStackTrace();
					}
				}
				if(rs!=null){

					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						logger.error(e);
						e.printStackTrace();
					}
					rs=null;	
				}
				
			}
			 
		
		return ttlist;
		
	}

	
	@Override
	public String updateT_ticket_trans_order(Connection conn,
			T_ticket_trans_order tt) {
		// TODO Auto-generated method stub
		
		 String sql="UPDATE T_TICKET_TRANS_ORDER SET IS_POINT=? ,POINT=? where TRANS_ID=?";
		 int flag=0;
		 PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		 try {
				//conn=db.getConnection();
				pst=conn.prepareStatement(sql);
				 
	 				pst.setString(1,tt.getIs_point());//会员身份目前生效的级别
	 				pst.setLong(2,tt.getPoint());//积分数
					pst.setLong(3,tt.getTrans_id());//生效之前的级别
					
					flag=pst.executeUpdate();
					conn.commit();
					 
				
			
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
					logger.error(e);
				 
				e.printStackTrace();
			}  finally {
				if(pst!=null){
					try {
						pst.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						logger.error(e);
						e.printStackTrace();
					}
				}
			}
		 
		 String fl ="";
		 fl = String.valueOf(flag);
		
		return fl;
	}


	@Override
	public long countT_ticket_trans_order(Connection conn,String is_point) {
		// TODO Auto-generated method stub
		
		 Date da = new Date();
		 String date = DateUtil.getDateStr(da);
			Calendar cal=Calendar.getInstance();
			int x=-2;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			
			String today=new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
			ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		
		Long seq = null  ;
	//	String sql = "SELECT COUNT(*) totalnum from   T_TICKET_TRANS_ORDER o where not exists(select set_time, order_id from "
	//		+"T_POINT_HISTORY t where to_char(t.set_time,'yyyymmdd') between '"+today+"' and  '"+date+"'and  t.order_id=o.order_id   ) and  IS_POINT=? ";
		String sql = " SELECT COUNT(*) totalnum from   T_TICKET_TRANS_ORDER o where   IS_POINT=? and point_amount<>0 ";
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		System.out.println(sql);
		try {
			pst=conn.prepareStatement(sql);
			  pst.setString(1,is_point);
			rs=pst.executeQuery();
			while (rs.next()) {
				 seq=(rs.getLong("totalnum"));
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 
				logger.error(e);
			 
			e.printStackTrace();
		}finally {
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);
					e.printStackTrace();
				}
			}
			if(rs!=null){

				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);
					e.printStackTrace();
				}
				rs=null;	
			}
			
		} 			
		return seq;
	}

}
