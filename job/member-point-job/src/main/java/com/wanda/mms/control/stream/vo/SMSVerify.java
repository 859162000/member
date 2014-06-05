package com.wanda.mms.control.stream.vo;

/**
 *  
 * 时实短信发送表
 * @author wangshuai 
 * @date 2013-06-03
 */
public class SMSVerify {

	/**
	 *短信ID#SMSID
	 */
	private long smsid;
	/**
	 *会员id#MEMBER_ID
	 */
	private long member_id;
	/**
	 *发送时间#DELIVERY_TIME
	 */
	private String delivery_time;
	/**
	 *有效时间#VALID_TIME
	 */
	private long valid_time;
	/**
	 *结束时间#END_TIME
	 */
	private String end_time;
	/**
	 *短信内容#MESSAGE_CONTENT
	 */
	private String message_content;
	/**
	 *数字校验码#DIGITAL_CHECKSUM
	 */
	private long digital_checksum;
	/**
	 *发送状态#SENDS_STATUS
	 */
	private String sends_status;
	/**
	 *校验状态#CHECKSUM_STATUS
	 */
	private String checksum_status;
	/**
	 *备注#REMARK
	 */
	private String remark;
	
	public long getSmsid() {
		return smsid;
	}
	public void setSmsid(long smsid) {
		this.smsid = smsid;
	}
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long memberId) {
		member_id = memberId;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String deliveryTime) {
		delivery_time = deliveryTime;
	}
	public long getValid_time() {
		return valid_time;
	}
	public void setValid_time(long validTime) {
		valid_time = validTime;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String endTime) {
		end_time = endTime;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String messageContent) {
		message_content = messageContent;
	}
	public long getDigital_checksum() {
		return digital_checksum;
	}
	public void setDigital_checksum(long digitalChecksum) {
		digital_checksum = digitalChecksum;
	}
	public String getSends_status() {
		return sends_status;
	}
	public void setSends_status(String sendsStatus) {
		sends_status = sendsStatus;
	}
	public String getChecksum_status() {
		return checksum_status;
	}
	public void setChecksum_status(String checksumStatus) {
		checksum_status = checksumStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	 
	
	
}
