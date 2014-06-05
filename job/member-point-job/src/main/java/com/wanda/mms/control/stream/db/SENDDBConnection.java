package com.wanda.mms.control.stream.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.util.ConfigReader;



public class SENDDBConnection {
	static Logger logger = Logger.getLogger(SENDDBConnection.class.getName());

	
	 private static final String RESOURCE_DB = "sendjdbc";
	 private static final  String NAME="sendjdbc.username";	  //定义一个name常量将数据库用户名作为字符串初值赋值 "ccs_stag"; 
	 private static final  String PSW="sendjdbc.password";	// 定义一个psw常量将数据库密码作为字符串初值赋值 "stagdb"; 
	 private static final  String URL="sendjdbc.url";	//定义一个URL常量将连接数据库字符串作为字符串初值赋值可以myeclipse中找到 "jdbc:oracle:thin:@10.199.200.242:1521:orcl"; 	
	 private static final  String DRIVER="sendjdbc.driverClassName";  //定义一个静态的driver常量将mysql数据库驱动地址作为字符串初值赋值可以myeclipse中找到 "oracle.jdbc.driver.OracleDriver";

 
	
 private static Connection conn=null;//定义一个静态数据库连接类型的变量conn初值为null
 
  private static final SENDDBConnection instance =new SENDDBConnection(); 
 	
 	public static SENDDBConnection getInstance(){
 		return instance;
 	}
 	
 	private SENDDBConnection(){
 		try {
 			ConfigReader config = new ConfigReader(RESOURCE_DB);
 			String driver = config.getValue(DRIVER);
 			String  url = config.getValue(URL);
 			String name  = config.getValue(NAME);
 			String psw  = config.getValue(PSW);
 	
			Class.forName(driver); //加载驱动类包 new一个Driver 向我们的DriverManager注册
//			new com.mysql.jdbc.Driver();
			System.out.println(url+"  url  "+name+"   "+psw);
			logger.info(url+"  url  "+name+"   "+psw);
			conn=DriverManager.getConnection(url, name, psw);	//通过DriverManager的getConnection方法向它传递JDBC连接
			//的URL并给这个连接用户名和密码连接指定的数据库并返回连接对象
			if (conn!=null) {
				logger.info("数据库连接成功");
				System.out.println("数据库连接成功");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
 	public Connection getConnection(){
 		return conn;
 	}
}
