package com.wanda.mms.control.stream.dao.impl;

 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.vo.Target;

 

public class TargetDaoImpl {

	static Logger logger = Logger.getLogger(TargetDaoImpl.class.getName());
	public List<Target> findTargetByIsSystemUser() {
		Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集

		List<Target> targetlist = new ArrayList<Target>();
		String sql = "select seqid,name,duty,mobile,rtx,issystemuser,CINEMA_INNER_CODE from T_IM_TARGET where CINEMA_INNER_CODE is null ";
		 
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
		 

			rs = pst.executeQuery();
			while (rs.next()) {
				Target ta = new Target();
				ta.setTargetId(rs.getLong("seqid"));
				ta.setName(rs.getString("name")); 
				ta.setDuty(rs.getString("duty"));
				ta.setMobile(rs.getString("mobile")); 
				ta.setIsSystemUser(rs.getInt("issystemuser")); 
				targetlist.add(ta);
			}

		} catch (SQLException e) {
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
		return targetlist;
	}
	public List<Target> findTargetByIsSystem(String cinema_inner_code) {
		

		Connection conn = null;// 数据数据库连接属性 conn，用于接收数据连接
		  PreparedStatement pst = null;// 数据库预先编译声明类型的属性 pst 用于操作数据库
		  ResultSet rs = null;// 结果集类型的 属性 rs 用于接收返回的结果集

		List<Target> targetlist = new ArrayList<Target>();
		String sql = "select seqid,name,duty,mobile,rtx,issystemuser,CINEMA_INNER_CODE from T_IM_TARGET where CINEMA_INNER_CODE=? ";
		SENDDBConnection db = SENDDBConnection.getInstance();
		conn = db.getConnection();

		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1,cinema_inner_code);//时间
			rs = pst.executeQuery();
			while (rs.next()) {
				Target ta = new Target();
				ta.setTargetId(rs.getLong("seqid"));
				ta.setName(rs.getString("name")); 
				ta.setDuty(rs.getString("duty"));
				ta.setMobile(rs.getString("mobile")); 
				ta.setIsSystemUser(rs.getInt("issystemuser")); 
				ta.setCinema_inner_code(rs.getString("CINEMA_INNER_CODE"));
				targetlist.add(ta);
			}

		} catch (SQLException e) {
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
		return targetlist;
	
	}
}
