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

import com.wanda.mms.control.stream.dao.T_goods_trans_orderDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.T_goods_trans_order;
import com.wanda.mms.control.stream.vo.T_ticket_trans_order;

public class T_goods_trans_orderDaoImpl implements T_goods_trans_orderDao {
	static Logger logger = Logger.getLogger(T_goods_trans_orderDaoImpl.class.getName());

	//private PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
	//private ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集


	@Override
	public long countT_goods_trans_order(Connection conn, String is_point) {
		// TODO Auto-generated method stub
		 Date da = new Date();
		 String date = DateUtil.getDateStr(da);
			Calendar cal=Calendar.getInstance();
			int x=-2;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			PreparedStatement pst=null;
			ResultSet rs=null;
			String today=new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	
		
		Long seq = null  ;
	//	String sql = "SELECT COUNT(*) totalnum   from T_GOODS_TRANS_ORDER o where not exists(select set_time, order_id from " 
	//		+" T_POINT_HISTORY t where to_char(t.set_time,'yyyymmdd') between '"+today+"' and  '"+date+"' and  t.order_id=o.order_id   ) and  IS_POINT=? ";
		String sql = "SELECT COUNT(*) totalnum   from T_GOODS_TRANS_ORDER o where IS_POINT=? ";
		
		
		//SENDDBConnection db=SENDDBConnection.getInstance();	
		//conn=db.getConnection();  
		
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
			}
			
		}	
		return seq;
	}

	@Override
	public List<T_goods_trans_order> fandT_goods_trans_orderPage(
			Connection conn, int point, Page page) {
		// TODO Auto-generated method stub
		 List<T_goods_trans_order> tglist = new ArrayList<T_goods_trans_order>();
		 PreparedStatement pst=null;
		 ResultSet rs=null;
 
			Calendar cal=Calendar.getInstance();
			int x=-2;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			
			String sql = "SELECT * FROM (  "
					+ " SELECT A.*, ROWNUM RN  "
					+ " FROM (select  GOODS_TRANS_ID,ORDER_ID,TOTAL_AMOUNT,MEMBER_NUM,IS_POINT,TRANS_TYPE,CINEMA_INNER_CODE,MEMBER_ID from T_GOODS_TRANS_ORDER o where  IS_POINT=? "						  
					+ "  　) A  "
					+ " WHERE ROWNUM <= ?) "
					+ " WHERE RN >= ? "; 
			//SENDDBConnection db=SENDDBConnection.getInstance();	
			//conn=db.getConnection();
			System.out.println(" ROWNUM ="+page.getPage() +"RN "+page.getPageSize()+"升降级= "+sql);
			try {
				pst=conn.prepareStatement(sql);
				pst.setInt(1,point);
				pst.setLong(2,page.getPageSize());
				pst.setLong(3,page.getPage());
				rs=pst.executeQuery();
				while (rs.next()) {
					T_goods_trans_order tg= new T_goods_trans_order();
					//会员卖品交易表ID
					tg.setGoods_trans_id(rs.getLong("GOODS_TRANS_ID"));
					//POS 订单ID
					tg.setOrder_id(rs.getString("ORDER_ID"));
					//卖品 金额  四舍五入成整数
					
					String ss = String.valueOf(rs.getDouble("TOTAL_AMOUNT"));
					BigDecimal d5 = new BigDecimal( ss  );
					String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
					//System.out.println(s6);
					long a = Long.valueOf(s6);
					tg.setTotal_amount(a);
					//会员手机号
					tg.setMember_num(rs.getString("MEMBER_NUM"));
					//是否计算 -- 状态
					tg.setIs_point(rs.getString("IS_POINT"));
					//是否退货
					tg.setTRANS_TYPE(rs.getString("TRANS_TYPE"));
					
					tg.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
					
					tg.setMemberId(rs.getLong("MEMBER_ID"));
					
					tglist.add(tg);
				}

		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			 
					logger.error(e);
				 
				e.printStackTrace();
			}	finally {
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
				}
				
			}
			 
		
		return tglist;
		
	}

	@Override
	public String updateT_goods_trans_order(Connection conn,
			T_goods_trans_order tg) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;

		 String sql="UPDATE T_GOODS_TRANS_ORDER SET IS_POINT=? ,POINT=? where GOODS_TRANS_ID=?";
		 int flag=0;
		 try {
			 
				//conn=db.getConnection();
				pst=conn.prepareStatement(sql);
				 
	 				pst.setString(1,tg.getIs_point());// 
	 				pst.setLong(2,tg.getPoint());// 
					pst.setLong(3,tg.getGoods_trans_id());// 
					
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
			}
		 
		 String fl ="";
		 fl = String.valueOf(flag);
		
		return fl;
	}

}
