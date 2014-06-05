package com.wanda.mms.control.stream.vo;

/**
 *  
 * 会员积分历史
 * @author wangshuai 
 * @date 2013-05-20
 */
public class MemberPoint {
	
	/**
	 *会员积分ID#member_point_id  
	 */
	private long member_point_id;
	/**
	 *会员ID#MEMBER_ID  
	 */
	private long memberId;
	/**
	 *可兑换积分余额#EXG_POINT_BALANCE 
	 */
	private long exg_Point_Balance;
	/**
	 *当年即将过期可兑换积分#EXG_EXPIRE_POINT_BALANCE
	 */
	private long exg_Expire_Point_Balance;
	/**
	 *最后发生积分历史ID#POINT_HISTORY_ID
	 */
	private long point_History_Id;
	/**
	 *总累计定积分#LEVEL_POINT_TOTAL 
	 */
	private long level_Point_Total;
	/**
	 *总累计非定级积分#ACTIVITY_POINT
	 */
	private long activity_Point;
	
	private String last_Exchange_Date;
	
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
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getExg_Point_Balance() {
		return exg_Point_Balance;
	}
	public void setExg_Point_Balance(long exgPointBalance) {
		exg_Point_Balance = exgPointBalance;
	}
	public long getExg_Expire_Point_Balance() {
		return exg_Expire_Point_Balance;
	}
	public void setExg_Expire_Point_Balance(long exgExpirePointBalance) {
		exg_Expire_Point_Balance = exgExpirePointBalance;
	}
	public long getPoint_History_Id() {
		return point_History_Id;
	}
	public void setPoint_History_Id(long pointHistoryId) {
		point_History_Id = pointHistoryId;
	}
	public long getLevel_Point_Total() {
		return level_Point_Total;
	}
	public void setLevel_Point_Total(long levelPointTotal) {
		level_Point_Total = levelPointTotal;
	}
	public long getActivity_Point() {
		return activity_Point;
	}
	public void setActivity_Point(long activityPoint) {
		activity_Point = activityPoint;
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
	public String getLast_Exchange_Date() {
		return last_Exchange_Date;
	}
	public void setLast_Exchange_Date(String lastExchangeDate) {
		last_Exchange_Date = lastExchangeDate;
	}
	public long getMember_point_id() {
		return member_point_id;
	}
	public void setMember_point_id(long memberPointId) {
		member_point_id = memberPointId;
	}
	
	

}
