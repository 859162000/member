package com.wanda.ccs.jobhub.smsreport.service;



public interface SendCinemaMemberSmsService {
	
	/**
	 * 发送影城短信
	 * 
	 * @param date
	 */
	public void sendCinemaSms(String date) throws Exception;

	public boolean checkStatus(int yyyyMMdd, String param);
	
	public void buildValid(String date, int yyyyMMdd) throws Exception;
	
	public void buildCinemaSend(int yyyyMMdd) throws Exception;
}