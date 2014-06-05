package com.wanda.mms.control.stream.vo;

 



/**
 *  
 *任务对列
 * @author wang 
 * @date 2013-04-11
 */
public class SendTask {
	
	private Long sendTaskId;
	/**
	 * 接收人
	 */
	private Long targetId;
	/**
	 * 信息发送内容ID
	 */
	private Long msgId;
	/**
	 * 信息发送时间
	 */
	private String  sendTime;//?时间
	/**
	 * 发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试
	 */
	private int status;
	/**
	 * 重试次数
	 */
	private int retryTimes;
	/**
	 * 冗余字段，接收者手机号
	 */
	private String targetMobile;  
	/**
	 * 信息发送类型。1-短信， 2-彩信 RTX？
	 */
	private int MsgType;//类型
	/**
	 * 发送优先级。数字越大优先级越高
	 */
	private int priority;//优先级
	
	/**
	 * 冗余字段，接收者RTX
	 */
	private String targetRTX;
	/**
	 *是否需要发送RTX.
	 *0-否(缺省)
	 *1-是
	 */
	private int needRTX ;
	
	public Long getSendTaskId() {
		return sendTaskId;
	}
	public void setSendTaskId(Long sendTaskId) {
		this.sendTaskId = sendTaskId;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	 
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	public String getTargetMobile() {
		return targetMobile;
	}
 
 
	public void setTargetMobile(String targetMobile) {
		this.targetMobile = targetMobile;
	}
	public int getMsgType() {
		return MsgType;
	}
	public void setMsgType(int msgType) {
		MsgType = msgType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getTargetRTX() {
		return targetRTX;
	}
	public void setTargetRTX(String targetRTX) {
		this.targetRTX = targetRTX;
	}
	public int getNeedRTX() {
		return needRTX;
	}
	public void setNeedRTX(int needRTX) {
		this.needRTX = needRTX;
	}
 
	
	

}
