package com.wanda.mms.control.stream.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 对PreparedStatement,进行释放
 * @author shichaomeng
 *
 */
public class ResultQuery {
	private ResultSet rs=null;
	private PreparedStatement ps = null;
	
	public ResultQuery(PreparedStatement ps,ResultSet rs){
		this.ps=ps;
		this.rs=rs;
	}
	public ResultSet getResultSet(){
		return rs;
	}
	
	public void free(){
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
