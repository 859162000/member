package com.wanda.ccs.member.segment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SMSConfig {

	public static String smsUrl = null;
	public static String smsProxyUrl = null;
	public static String smsFromsys = null;
	public static String smsTitle = null;
	public static String msgMqIp = null;
	public static String msgSvcIp = null;
	public static String msgChannelId= null;
	public void initSMSConfig() {
		Properties prop = new Properties();
		InputStream in = null;
		try {
//			in = new BufferedInputStream(new FileInputStream(
//					"messageConfig.properties"));
			in = getClass().getClassLoader().getResourceAsStream("messageConfig.properties");
			prop.load(in);
			smsUrl = prop.getProperty("SMS_URL");
			smsProxyUrl = prop.getProperty("HP_SMS_PROXY_URL");
			smsFromsys = prop.getProperty("SMS_FROMSYS");
			smsTitle = prop.getProperty("CARD_SMS_TITLE");
			msgMqIp = prop.getProperty("msgMqIp");
			msgSvcIp = prop.getProperty("msgSvcIp");
			msgChannelId= prop.getProperty("msgChannelId");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] agrs){
		System.out.println(smsTitle);
	}
}
