package com.wanda.mrb.intf.bean;

public class MsgIpConfigBean {
	private String msgMqIp;
	private String msgMqPort;
	private String msgSvcIp;
	private String msgSvcPort;
	private String msgChannelId;
	public String getMsgMqIp() {
		return msgMqIp;
	}
	public void setMsgMqIp(String msgMqIp) {
		this.msgMqIp = msgMqIp;
	}
	public String getMsgMqPort() {
		return msgMqPort;
	}
	public void setMsgMqPort(String msgMqPort) {
		this.msgMqPort = msgMqPort;
	}
	public String getMsgSvcIp() {
		return msgSvcIp;
	}
	public void setMsgSvcIp(String msgSvcIp) {
		this.msgSvcIp = msgSvcIp;
	}
	public String getMsgSvcPort() {
		return msgSvcPort;
	}
	public void setMsgSvcPort(String msgSvcPort) {
		this.msgSvcPort = msgSvcPort;
	}
	public String getMsgChannelId() {
		return msgChannelId;
	}
	public void setMsgChannelId(String msgChannelId) {
		this.msgChannelId = msgChannelId;
	}
}
