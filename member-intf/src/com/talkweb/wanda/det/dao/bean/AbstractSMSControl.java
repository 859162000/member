package com.talkweb.wanda.det.dao.bean;
/*
 * Copyright (c) 2007-2009 talkWeb All rights reserved.
 * History: 
 * Date          Modified_By    Reason    Description 
 * 2011-01-04    xiaowenbin       
 */


import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;






/**
 * @author xiaowenbin
 */

public  class AbstractSMSControl {
	 
	private String hostKey;

	private String portKey;

	public  List<Sms> sendList;

	Properties prop = System.getProperties();

//	private String ip;

//	private int port;

	/**
	 *  
	 */
	public void init() {
		sendList = new java.util.ArrayList<Sms>();
	//	ip = com.talkweb.wanda.SpringParameter.getValue(hostKey);
	//	port = Integer.parseInt(com.talkweb.wanda.SpringParameter.getValue(portKey));
	//	log.info(this.getClass().getName() + " ip==" + ip + "&port==" + port);
	//	ip ="10.0.8.55";
	//	port = 20003;
		Thread sendMms=new Thread(new SendMMSThread());
		sendMms.start();
	}

	public void addSms(Sms sms) {
		sendList.add(sendList.size(), sms);
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}

	public String getPortKey() {
		return portKey;
	}

	public void setPortKey(String portKey) {
		this.portKey = portKey;
	}

	public Sms doGetSms() {
		Sms sms = null;
		if (sendList != null && sendList.size() > 0) {
			sms = sendList.get(0);
			sendList.remove(0);
		}
		return sms;
	}
	
	class SendMMSThread implements Runnable{
		
		/**
		 *  
		 * @param times
		 */
		public void timeSleep(long times){
			try{
				Thread.sleep(times);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/**
		 * 
		 * @param sms
		 * @param i
		 */
		public boolean printSms(Sms sms,int i){
			if(i>=3){
			//	log.info(" "+sms.getMobileNo()+", :"+sms.getMo()+",:"+sms.getMtAddr());
				return true;
			}
			return false;
		}
		/**
		 * 
		 * @param sms
		 */
		public void sendSms(Sms sms,int i,String ip,int port){
			if(printSms(sms, i)){
				return;
			}
			
//			prop.setProperty("http.proxyHost", "proxy1.wanda.cn");
//			prop.setProperty("http.proxyPort", "8080");
//			prop.setProperty("http.proxyUser", "v_lvjinfeng");
//			prop.setProperty("http.proxyPassword","123321");
			Socket socket = null;
			ObjectOutputStream objectOutputStream = null;
			OutputStream outputStream = null;
			try {
			//	log.info("Socket ...IP==" + ip + "&port=" + port);
				 
				
			//	socket = new Socket();
				 
				socket = new Socket(ip, port);
				
				outputStream = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(sms);
				objectOutputStream.flush();
				System.out.println("");
			} catch (Exception e) {
				//if(!log.isDebugEnabled()){
				//	log.error(e);
				//}
				e.printStackTrace();
				System.out.println("");
				timeSleep(20000);
				sendSms(sms,i++, ip, port);
			} finally {
				try {
					if (objectOutputStream != null) {
						objectOutputStream.close();
					}
				} catch (Exception e) {
//					if(!log.isDebugEnabled()){
//						log.error(e);
//					}
				}
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
//					if(!log.isDebugEnabled()){
//						log.error(e);
//					}
				}
				try {
					socket.close();
				} catch (Exception e) {
//					if (!log.isDebugEnabled()) {
//						log.error(e);
//					}
				}
				//removeProxy();
				
			}
		}
		/**
		 * 去掉代理 为了防止其它模块使用系统代理时出错
		 */
		public void removeProxy() {
			// 设置http访问要使用的代理服务器的地址
			prop.setProperty("http.proxyHost", "");
			// 设置http访问要使用的代理服务器的端口
			prop.setProperty("http.proxyPort", "");
			// 设置http访问要使用的代理服务器的用户名
			prop.setProperty("http.proxyUser", "");
			// 设置http访问要使用的代理服务器的密码
			prop.setProperty("http.proxyPassword", "");
		}

		public void run() {
			while(true){
				Sms sms=doGetSms();
				if(sms==null){
					timeSleep(2000l);
					continue;
				}
				timeSleep(2l);
				sendSms(sms,0, "10.0.8.55", 20003);
				
			}
			
		}
		
	}

	public List<Sms> getSendList() {
		return sendList;
	}

	public void setSendList(List<Sms> sendList) {
		this.sendList = sendList;
	}

	 

}
