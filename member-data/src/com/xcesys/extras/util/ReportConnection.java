package com.xcesys.extras.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.xcesys.extras.core.exception.ApplicationException;

public class ReportConnection {

	public static Connection newConnection() {
		Connection connection = null;
		Properties prop = new Properties();
		try {
			prop.load(ReportConnection.class.getClassLoader().getResourceAsStream(
					"jdbc-local.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("读取数据可连接配置文件!"
					+ e.getLocalizedMessage());
		}
		try {

			Class.forName(prop.getProperty("report.driverClassName"))
					.newInstance();
			String url = prop.getProperty("report.url");
			String user = prop.getProperty("report.username");
			String password = prop.getProperty("report.password");

			connection = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("报表系统数据可连接打开失败!"
					+ e.getLocalizedMessage());
		}
		return connection;
	}
}
