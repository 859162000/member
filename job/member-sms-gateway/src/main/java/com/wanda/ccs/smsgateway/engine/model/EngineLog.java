package com.wanda.ccs.smsgateway.engine.model;

import java.util.Date;

public class EngineLog {
	private Long logId;
	private String engineName;
	private String deliveryMsg;
	private String taskId;
	private String taskName;
	private String taskState;
	private Date taskCreateTime;
	private Date taskCompleteTime;

	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
	public String getDeliveryMsg() {
		return deliveryMsg;
	}
	public void setDeliveryMsg(String deliveryMsg) {
		this.deliveryMsg = deliveryMsg;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public Date getTaskCreateTime() {
		return taskCreateTime;
	}
	public void setTaskCreateTime(Date taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}
	public Date getTaskCompleteTime() {
		return taskCompleteTime;
	}
	public void setTaskCompleteTime(Date taskCompleteTime) {
		this.taskCompleteTime = taskCompleteTime;
	}

}
