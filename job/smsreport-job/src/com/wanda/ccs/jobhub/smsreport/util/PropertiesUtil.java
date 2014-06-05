package com.wanda.ccs.jobhub.smsreport.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 执行状态文件读取
 * 
 * @author zhurui
 * @date 2014年1月9日 上午9:50:49
 */
public class PropertiesUtil {
	
	public static final Logger logger = Logger.getLogger(PropertiesUtil.class);
	
	private static Properties prop;
	
	public static PropertiesUtil util = new PropertiesUtil();
	
	private PropertiesUtil() {
		InputStream in = null;
		try {
			in = PropertiesUtil.class.getClassLoader().getResourceAsStream("conf.properties");
			prop = new Properties();
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getValueByKey(String key) {
		return prop.getProperty(key);
	}
	
	public static void setValueByKey(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println(PropertiesUtil.getValueByKey("line.date"));
		PropertiesUtil.setValueByKey("line.date", "11111");
		System.out.println(PropertiesUtil.getValueByKey("line.date"));
		Thread.sleep(5000);
		System.out.println(PropertiesUtil.getValueByKey("line.date"));
	}

}
