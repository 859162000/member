package com.wanda.ccs.jobhub.member.vo;


/**
 *  
 * 会员积分历史
 * @author Charlie Zhang 
 * @date 2013-12-21
 */
public class MemberPointVo extends BaseAuditVo {
	
	private static final long serialVersionUID = 1627621651114867065L;

	private Long memberPointId;
	
	private Long memberId;
	
	private Long exgPointBalance;
	
	private Long exgExpirePointBalance;
	
	private Long pointHistoryId;

	private Long levelPointTotal;

	private Long activityPoint;
	
	private java.sql.Timestamp lastExchangeDate;
	
	private Boolean isdelete;
	
	private Boolean isLevel;

	
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public Long getMemberPointId() {
		return memberPointId;
	}
	public void setMemberPointId(Long memberPointId) {
		this.memberPointId = memberPointId;
	}
	public Long getExgPointBalance() {
		return exgPointBalance;
	}
	public void setExgPointBalance(Long exgPointBalance) {
		this.exgPointBalance = exgPointBalance;
	}
	public Long getExgExpirePointBalance() {
		return exgExpirePointBalance;
	}
	public void setExgExpirePointBalance(Long exgExpirePointBalance) {
		this.exgExpirePointBalance = exgExpirePointBalance;
	}
	public Long getPointHistoryId() {
		return pointHistoryId;
	}
	public void setPointHistoryId(Long pointHistoryId) {
		this.pointHistoryId = pointHistoryId;
	}
	public Long getLevelPointTotal() {
		return levelPointTotal;
	}
	public void setLevelPointTotal(Long levelPointTotal) {
		this.levelPointTotal = levelPointTotal;
	}
	public Long getActivityPoint() {
		return activityPoint;
	}
	public void setActivityPoint(Long activityPoint) {
		this.activityPoint = activityPoint;
	}
	public java.sql.Timestamp getLastExchangeDate() {
		return lastExchangeDate;
	}
	public void setLastExchangeDate(java.sql.Timestamp lastExchangeDate) {
		this.lastExchangeDate = lastExchangeDate;
	}
	public Boolean getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}
	public Boolean getIsLevel() {
		return isLevel;
	}
	public void setIsLevel(Boolean isLevel) {
		this.isLevel = isLevel;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String retrieveCode() {
		return getMemberPointId().toString();
	}
	public Long retrieveSeqId() {
		return getMemberPointId();
	}
	
	

}
