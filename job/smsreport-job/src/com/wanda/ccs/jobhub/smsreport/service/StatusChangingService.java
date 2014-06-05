package com.wanda.ccs.jobhub.smsreport.service;

public interface StatusChangingService {

	/**
	 * 根据开始时间和结束时间更新各个表的状态
	 * @return
	 */
	void updateStatus();

}