package com.wanda.mms.control.stream.vo;
/**
 *  
 * 会员级别
 * @author wangshuai 
 * @date 2013-05-23
 */
public class MemberLevel {
	
	/**
	 *会员ID#MEMBER_ID
	 */
	private long member_id;
	/**
	 *当前级别#MEM_LEVEL
	 */	
	private String mem_level;
	/**
	 *当前级别有效期#EXPIRE_DATE
	 */	
	private String expire_date;
	/**
	 *上一级别#ORG_LEVEL
	 */	
	private String org_level;	
	/**
	 *级别变更时间#SET_TIME
	 */		
	private String set_time;	
	/**
	 *目标级别#TARGET_LEVEL
	 */		
	private String target_level;	
	/**
	 *升级至目标级别差距(积分)#LEVEL_POINT_OFFSET
	 *还差xx积分升级至目标级别：
	 *会员升级计算后更新
	 */		
	private long level_point_offset;
	/**
	 *升级至目标级别差距(影票)#TICKET_OFFSET
	 */		
	private long ticket_offset;
	/**
	 *级别变更批次号
	 */	
	private String change_level_no;
	/**
	 *最后级别历史流水#LAST_LEVEL_HISTORY_ID	
	 */	
	private long last_level_history_id;
	/**
	 *逻辑删除标识#ISDELETE
	 */
	private int isdelete;
	/**
	 *创建者#CREATE_BY
	 */
	private String create_By;
	/**
	 *创建时间#CREATE_DATE
	 */
	private String create_Date;
	/**
	 *更新者#UPDATE_BY
	 */
	private String update_By;
	/**
	 *更新时间#UPDATE_DATE
	 */
	private String update_Date;
	/**
	 *版本号#VERSION
	 */
	private int version;
	/**
	 *历史表ID
	 */
	private long member_level_history_id;
	
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long memberId) {
		member_id = memberId;
	}
	public String getMem_level() {
		return mem_level;
	}
	public void setMem_level(String memLevel) {
		mem_level = memLevel;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(String expireDate) {
		expire_date = expireDate;
	}
	public String getOrg_level() {
		return org_level;
	}
	public void setOrg_level(String orgLevel) {
		org_level = orgLevel;
	}
	public String getSet_time() {
		return set_time;
	}
	public void setSet_time(String setTime) {
		set_time = setTime;
	}
	public String getTarget_level() {
		return target_level;
	}
	public void setTarget_level(String targetLevel) {
		target_level = targetLevel;
	}
	public long getLevel_point_offset() {
		return level_point_offset;
	}
	public void setLevel_point_offset(long levelPointOffset) {
		level_point_offset = levelPointOffset;
	}
	public long getTicket_offset() {
		return ticket_offset;
	}
	public void setTicket_offset(long ticketOffset) {
		ticket_offset = ticketOffset;
	}
	public String getChange_level_no() {
		return change_level_no;
	}
	public void setChange_level_no(String changeLevelNo) {
		change_level_no = changeLevelNo;
	}
	public long getLast_level_history_id() {
		return last_level_history_id;
	}
	public void setLast_level_history_id(long lastLevelHistoryId) {
		last_level_history_id = lastLevelHistoryId;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public String getCreate_By() {
		return create_By;
	}
	public void setCreate_By(String createBy) {
		create_By = createBy;
	}
	public String getCreate_Date() {
		return create_Date;
	}
	public void setCreate_Date(String createDate) {
		create_Date = createDate;
	}
	public String getUpdate_By() {
		return update_By;
	}
	public void setUpdate_By(String updateBy) {
		update_By = updateBy;
	}
	public String getUpdate_Date() {
		return update_Date;
	}
	public void setUpdate_Date(String updateDate) {
		update_Date = updateDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getMember_level_history_id() {
		return member_level_history_id;
	}
	public void setMember_level_history_id(long memberLevelHistoryId) {
		member_level_history_id = memberLevelHistoryId;
	}
	
 
	
}
