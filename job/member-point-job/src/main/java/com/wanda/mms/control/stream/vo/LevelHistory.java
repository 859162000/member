package com.wanda.mms.control.stream.vo;

/**
 *  
 * 会员级别历史 
 * @author wangshuai 
 * @date 2013-05-23
 */
public class LevelHistory {
	
 
	/**
	 *会员级别历史ID#LEVEL_HISTORY_ID
	 */
	private long level_history_id;
	/**
	 *变更后级别#MEM_LEVEL
	 */	
	private String mem_level;
	/**
	 *变更后级别有效期#EXPIRE_DATE
	 */	
	private String expire_date;
	/**
	 *变更前级别#ORG_MEM_LEVEL
	 */	
	private String org_mem_level;	
	/**
	 *变更前级别有效期#ORG_EXPIRE_DATE
	 */		
	private String org_expire_date;	
	/**
	 *级别变更时间#SET_TIME
	 */		
	private String set_time;	
	/**
	 *变更原因类型#RESON_TYPE
	 */	
	private String reson_type;	
	/**
	 *原因描述#REASON
	 */	
	private String REASON;	
	/**
	 *变更类型#CHG_TYPE
	 */	
	private String chg_type;
	/**
	 *会员ID#MEMBER_ID
	 */
	private long member_id;
	/**
	 *判定的定级积分数#LEVEL_POINT
	 */	
	private long level_point;
	/**
	 *判定的有效影票数#TICKET_COUNT
	 */	
	private long ticket_count;
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
	public long getLevel_history_id() {
		return level_history_id;
	}
	public void setLevel_history_id(long levelHistoryId) {
		level_history_id = levelHistoryId;
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
	public String getOrg_mem_level() {
		return org_mem_level;
	}
	public void setOrg_mem_level(String orgMemLevel) {
		org_mem_level = orgMemLevel;
	}
	public String getOrg_expire_date() {
		return org_expire_date;
	}
	public void setOrg_expire_date(String orgExpireDate) {
		org_expire_date = orgExpireDate;
	}
	public String getSet_time() {
		return set_time;
	}
	public void setSet_time(String setTime) {
		set_time = setTime;
	}
	public String getReson_type() {
		return reson_type;
	}
	public void setReson_type(String resonType) {
		reson_type = resonType;
	}
 
	public String getREASON() {
		return REASON;
	}
	public void setREASON(String rEASON) {
		REASON = rEASON;
	}
	public String getChg_type() {
		return chg_type;
	}
	public void setChg_type(String chgType) {
		chg_type = chgType;
	}
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long memberId) {
		member_id = memberId;
	}
	public long getLevel_point() {
		return level_point;
	}
	public void setLevel_point(long levelPoint) {
		level_point = levelPoint;
	}
	public long getTicket_count() {
		return ticket_count;
	}
	public void setTicket_count(long ticketCount) {
		ticket_count = ticketCount;
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
	
	

}
