/**  
* @Title: SegmentMessageVo.java
* @Package com.wanda.ccs.member.segment.vo
* @Description: TODO(用一句话描述该文件做什么)
* @author 许雷
* @date 2015年5月21日 上午10:46:48
* @version V1.0  
*/
package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName: SegmentMessageVo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 许雷
 * @date 2015年5月21日 上午10:46:48
 *
 */
public class SegmentMessageVo  implements Serializable{
	
	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/
	
	private static final long serialVersionUID = -6204417627072527701L;
	
	private  Long segmMessageId;
	
	private  String content;
	
	private  String approveStatus;
	
	private  String segmentId;
	
	private  String sendTime;
	
	private  String createBy;
	
	private  String updateBy;
	
	private  Timestamp createDate;
	
	private  Timestamp updateDate;
	
	private  long noSendCal;
	
	private  long send_status;
	
	private String allowModifier;
	
	private String occupied;
	
	private String approver;
	
	private String cinema;
	
	private String batchId;
	
	private String wordContent;
	
	public String getCinema() {
		return cinema;
	}
	public void setCinema(String cinema) {
		this.cinema = cinema;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	private String area;
	
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public long getSend_status() {
		return send_status;
	}
	public void setSend_status(long send_status) {
		this.send_status = send_status;
	}
	public long getNoSendCal() {
		return noSendCal;
	}
	public void setNoSendCal(long noSendCal) {
		this.noSendCal = noSendCal;
	}
	private  String version;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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

	public Long getSegmMessageId() {
		return segmMessageId;
	}
	public void setSegmMessageId(Long segmMessageId) {
		this.segmMessageId = segmMessageId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getAllowModifier() {
		return allowModifier;
	}
	public void setAllowModifier(String allowModifier) {
		this.allowModifier = allowModifier;
	}
	public String getOccupied() {
		return occupied;
	}
	public void setOccupied(String occupied) {
		this.occupied = occupied;
	}

	public String getBatchId() {
		return batchId;
	}
	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getWordContent() {
		return wordContent;
	}
	public void setWordContent(String wordContent) {
		this.wordContent = wordContent;
	}
	
	
}
