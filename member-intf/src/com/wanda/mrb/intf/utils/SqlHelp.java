package com.wanda.mrb.intf.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * sql操作简单封装
 * @author shichaomeng
 *
 */
public class SqlHelp {
	
	/**
	 * 增删改操作
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public static int operate(Connection conn ,final String sql,final String... params) throws SQLException{
		int result =0;
		PreparedStatement ps = null;
			ps=conn.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length; i++){
					ps.setString(i+1, params[i]);
				}
			}
			result =ps.executeUpdate();
		free(null,ps,null,null);
		return result;
	}
	
	/**
	 * 执行查询操作
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public static ResultQuery query(Connection conn ,final String sql,final String... params) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for(int i=0;i<params.length; i++){
					ps.setString(i+1, params[i]);
				}
			}
			rs = ps.executeQuery();
		return new ResultQuery(ps,rs);
	}
	
	/**
	 * 释放资源
	 * @param rs
	 * @param ps
	 * @param st
	 * @param conn
	 */
	public static void free(ResultSet rs, PreparedStatement ps, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
