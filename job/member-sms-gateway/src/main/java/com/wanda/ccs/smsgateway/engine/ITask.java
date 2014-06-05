package com.wanda.ccs.smsgateway.engine;

public interface ITask extends Runnable {
	public void setLogId(Long logId);
	public void setEngineName(String engineName);
	public void setOriginMsg(String originMsg);
	public void setChannelId(String channelId);
	public void setSystemId(String systemId);
	public void setNoPrefixMsg(String noPrefixMsg);
	public String getTaskName();
}
