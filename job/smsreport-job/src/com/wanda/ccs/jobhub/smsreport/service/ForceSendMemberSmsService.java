package com.wanda.ccs.jobhub.smsreport.service;

public interface ForceSendMemberSmsService {
	
	/**
	 * 手动发送短信
	 * 
	 * @param date
	 * @param type				line 院线		cinema 影城
	 * @param sendType			1 重新统计发送 	2只是发送不统计 
	 * @throws SystemException
	 */
	public void forceSend(String date ,String type, int sendType, boolean valid) throws Exception;
	
}