package com.wanda.ccs.jobhub.smsreport.vo;

import java.io.Serializable;

/**
 * 人员短信消息关联表
 * 
 * @author zhurui
 * @date 2014年1月7日 下午3:46:38
 */
public class TargetSmsRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	/* 接收人ID */
	private Long targetId;
	/* 信息发送内容ID */
	private Long msgId;
	/* 信息发送时间 */
	private String sendTime;
	/* 发送状态. 0-带发送,1-发送成功,2-发送中,3-失败重试 */
	private int status;
	/* 重试次数 */
	//private int retryTimes;
	/* 冗余字段，接收者手机号 */
	private String mobile;
	/* 信息发送类型。1-短信， 2-彩信 RTX */
	//private int MsgType;// 类型
	/* 发送优先级。数字越大优先级越高 */
	private int priority;
	/* 冗余字段，接收者RTX */
	//private String targetRTX;
	/* 是否需要发送RTX 0-否(缺省) 1-是 */
	//private int needRTX;

	public TargetSmsRelation() {}
	
	public TargetSmsRelation(Long id, Long targetId, Long msgId, String mobile) {
		this.id = id;
		this.targetId = targetId;
		this.msgId = msgId;
		this.mobile = mobile;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
