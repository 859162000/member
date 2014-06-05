package com.wanda.ccs.smsgateway.engine.dao;

import org.apache.ibatis.annotations.Param;

import com.wanda.ccs.smsgateway.engine.model.EngineLog;

public interface EngineLogDAO {
	public void insertEngineLog(EngineLog log);
	public void updateTaskState(@Param("logId")Long logId, @Param("taskName")String taskName, @Param("state")int state);
	public void insertTaskError(@Param("logId")Long logId, @Param("errorMsg")String errorMsg);
	public void insertEngineError(@Param("logId")Long logId, @Param("errorMsg")String errorMsg);
}
