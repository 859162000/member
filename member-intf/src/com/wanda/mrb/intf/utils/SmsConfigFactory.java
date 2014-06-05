package com.wanda.mrb.intf.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.wanda.mrb.intf.SQLConstDef;

/**
 * @author xuesi
 *
 */
public class SmsConfigFactory {
	
	static Map<String,String> msgIpMap;
	
	private SmsConfigFactory(){
	}
	
	public static Map<String,String> getSmsConfigInstance(Connection conn){
		HashMap<String,String> msgIpMap = new HashMap<String,String>();
		if (msgIpMap.size() == 0) {
			try {
				PreparedStatement ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_CONFIG);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					msgIpMap.put(rs.getString("PARAMETER_NAME"), rs.getString("PARAMETER_VALUE"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return msgIpMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
