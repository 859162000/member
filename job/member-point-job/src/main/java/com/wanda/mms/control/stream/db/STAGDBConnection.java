package com.wanda.mms.control.stream.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.wanda.mms.control.stream.util.ConfigReader;


public class STAGDBConnection {
	 private static final String RESOURCE_DB = "sendjdbc";
	 private static final  String NAME="shrjdbc.username";	  
	 private static final  String PSW="shrjdbc.password";	 
	 private static final  String URL="shrjdbc.url";	 
	 private static final  String DRIVER="sendjdbc.driverClassName";  

	
 private static Connection conn=null; 
 
  private static final STAGDBConnection instance =new STAGDBConnection(); 
 
 	public static STAGDBConnection getInstance(){
 		return instance;
 	}
 	
 	private STAGDBConnection(){
 		try {
 			ConfigReader config = new ConfigReader(RESOURCE_DB);
 			String driver = config.getValue(DRIVER);
 			String  url = config.getValue(URL);
 			String name  = config.getValue(NAME);
 			String psw  = config.getValue(PSW);
 	
			Class.forName(driver); 
			conn=DriverManager.getConnection(url, name, psw);	
			if (conn!=null) {
				System.out.println("shrjdbc连接  "+url);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
 	}
 	public Connection getConnection(){
 		return conn;
 	}
}
