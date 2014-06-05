package com.wanda.ccs.jobhub.smsreport.service;

import java.util.Map;



public interface SendLineMemberSmsService {

	/**
	 * 发送院线短信
	 * 
	 * @param date
	 */
	public void sendLineSms(String date) throws Exception;
	
	public boolean checkStatus(int yyyyMMdd, String param);
	
	public Map<String, Integer> getStatus(int yyyyMMdd);
	
	public void buildValid(String date, int yyyyMMdd) throws Exception;
	
	public void buildLineSend(int yyyyMMdd) throws Exception;
	
}