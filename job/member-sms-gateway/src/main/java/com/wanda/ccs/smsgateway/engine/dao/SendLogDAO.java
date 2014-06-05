package com.wanda.ccs.smsgateway.engine.dao;

import org.apache.ibatis.annotations.Param;

import com.wanda.ccs.smsgateway.engine.model.SendLog;

public interface SendLogDAO {
	public void insertSendLog(SendLog sendLog);
	
	public void updateAddQueueSuccess(long sendId);
	public void updateAddQueueFailed(long sendId);
	
	public int querySendedCount(@Param("mobileNo")String mobileNo, @Param("second")String second);
}
