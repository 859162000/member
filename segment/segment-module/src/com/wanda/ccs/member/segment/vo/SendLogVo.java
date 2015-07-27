/**  
 * @Title: SendLogVo.java
 * @Package com.wanda.ccs.member.segment.vo
 * @Description: 信息发送日志类
 * @author 许雷
 * @date 2015年6月23日 上午9:37:16
 * @version V1.0  
 */
package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName: SendLogVo
 * @Description: 信息发送日志类
 * @author 许雷
 * @date 2015年6月23日 上午9:37:16
 *
 */
public class SendLogVo implements Serializable {

	/**
	 * @Fields serialVersionUID : 序列化属性
	 */
	private static final long serialVersionUID = 8729347809802853585L;

	private long sendLogId;
	private String sendStatus;
	private Timestamp createDate;
	private Timestamp updateDate;
	private Timestamp startTime;
	private Timestamp endTime;
	private long segm_messageId;
	private long sendCount;

	public long getSendLogId() {
		return sendLogId;
	}

	public void setSendLogId(long sendLogId) {
		this.sendLogId = sendLogId;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public long getSegm_messageId() {
		return segm_messageId;
	}

	public void setSegm_messageId(long segm_messageId) {
		this.segm_messageId = segm_messageId;
	}

	public long getSendCount() {
		return sendCount;
	}

	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
}
