package com.wanda.mms.control.stream.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.T_segmentDao;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.T_ext_point_rule;
import com.wanda.mms.control.stream.vo.T_segment;

public class TmpDaoImpl{
	static Logger logger = Logger.getLogger(TmpDaoImpl.class.getName());

	
	public void tmp(Connection conn,String cinema) {
		// TODO Auto-generated method stub
		 
		  PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
		
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		 Long seq = null  ;
			String sql = "delete from TMP_EXT_TRANS_DETAIL t where CINEMA_INNER_CODE=?"; 
	 		try {
				pst=conn.prepareStatement(sql);
				pst.setString(1,cinema);
				rs=pst.executeQuery();
		
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
			 
		
		 
		
	}
	
	public List<String> fandCinema_inner_code(Connection conn,String date){	//查询今天有多少影城要计算识分
		List<String> strlist = new ArrayList<String>();
		 PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
			
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		 Long seq = null  ;
			String sql = "select t.cinema_inner_code  from T_TICKET_TRANS_DETAIL t  where 　 t.biz_date < to_date(?, 'yyyy-mm-dd') + 1 and   t.biz_date >= to_date(?, 'yyyy-mm-dd')  group by cinema_inner_code"; 
	 		try {
				pst=conn.prepareStatement(sql);
				pst.setString(1,date);
				pst.setString(2,date);
				System.out.println(sql);
				System.out.println(date);
				rs=pst.executeQuery();
				 
				while (rs.next()) {
					String code =	 rs.getString("CINEMA_INNER_CODE");
					strlist.add(code);
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
		
		
		return strlist;
	}
	
	public String fandsysdatajob(Connection conn,String date){	//查询今天有多少影城要计算识分
		String strlist = "select j.ymd, j.flag_mbr,j.flag_mbr_points_repair,j.time_mbr, "
     +"  j.time_mbr_repair, j.flag_mbr_points, j.time_mbr_points, j.seqid   "
     +"  from CCS_REPORT.T_SYS_DATA_JOB j  where ymd=? and FLAG_MBR=1 and FLAG_ETL_SQL=1";
		 PreparedStatement pst=null;//  数据库预先编译声明类型的属性 pst 用于操作数据库
			
		  ResultSet rs=null;// 结果集类型的 属性 rs 用于接收返回的结果集

		 Long seq = null  ;
			String sql = " "; 
	 		try {
				pst=conn.prepareStatement(sql);
				pst.setString(1,date);
				pst.setString(1,date);
				rs=pst.executeQuery();
				 
				while (rs.next()) {
					String code =	 rs.getString("CINEMA_INNER_CODE");
					 
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
		
		
		return strlist;
	}
	
	

}
