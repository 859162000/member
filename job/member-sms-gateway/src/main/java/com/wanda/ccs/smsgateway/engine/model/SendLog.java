package com.wanda.ccs.smsgateway.engine.model;

import java.util.Date;

public class SendLog {
	private Long sendId;
	private Long logId;
	private String engineName;
	private String taskName;
	private String mobileNo;
	private String sendMsg;
	private String serviceUp;
	private String spNumber;
	private String linkId;
	private String systemId;
	private String settleId;
	private String channelId;
	private String platform;
	private int addQueueState;
	private Date addQueueTime;
	private Date create_time;
	
	public SendLog(Long logId, String engineName) {
		super();
		this.logId = logId;
		this.engineName = engineName;
	}
	public Long getSendId() {
		return sendId;
	}
	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSendMsg() {
		return sendMsg;
	}
	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
	public String getServiceUp() {
		return serviceUp;
	}
	public void setServiceUp(String serviceUp) {
		this.serviceUp = serviceUp;
	}
	public String getSpNumber() {
		return spNumber;
	}
	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public int getAddQueueState() {
		return addQueueState;
	}
	public void setAddQueueState(int addQueueState) {
		this.addQueueState = addQueueState;
	}
	public Date getAddQueueTime() {
		return addQueueTime;
	}
	public void setAddQueueTime(Date addQueueTime) {
		this.addQueueTime = addQueueTime;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSettleId() {
		return settleId;
	}
	public void setSettleId(String settleId) {
		this.settleId = settleId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

}
