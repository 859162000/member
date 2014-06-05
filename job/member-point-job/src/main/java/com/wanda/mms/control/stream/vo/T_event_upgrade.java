package com.wanda.mms.control.stream.vo;

public class T_event_upgrade {
	
	private long seq_id;
	/**
	 * 会员ID
	 */
	private long member_id;
	/**
	 * 当前级别，就是升级后的级别
	 */
	private String member_level;
	/**
	 * 0：未发送，1：已发送
	 */
	private String status;
	/**
	 * 任务创建时间
	 */
	private String create_time;
	public long getSeq_id() {
		return seq_id;
	}
	public void setSeq_id(long seqId) {
		seq_id = seqId;
	}
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long memberId) {
		member_id = memberId;
	}
	public String getMember_level() {
		return member_level;
	}
	public void setMember_level(String memberLevel) {
		member_level = memberLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	@Override
	public String toString() {
		return "T_event_upgrade [任务创建时间=" + create_time + ", 会员ID="
				+ member_id + ", 等级=" + member_level + ", seq_id="
				+ seq_id + ",已发送状态=" + status + "]";
	}
	
	

}
