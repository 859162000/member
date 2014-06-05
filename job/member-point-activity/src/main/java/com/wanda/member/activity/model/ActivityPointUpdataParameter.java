package com.wanda.member.activity.model;

public class ActivityPointUpdataParameter {
	private String ruleType;
	private String pointHistoryId;
	private String memberKey; 
	private int activityPoint; 
	private String innerCode; 
	private String orderCode; 
	private String item; 
	private String adjResion; 
	private int extPointRuleId; 
	private int extPointCriteriaId;
	private String setTime;
	private int orgPoint;
	private int pointBlance;
	private String batchId;
	private String transType;
	public String getMemberKey() {
		return memberKey;
	}
	public void setMemberKey(String memberKey) {
		this.memberKey = memberKey;
	}
	public int getActivityPoint() {
		return activityPoint;
	}
	public void setActivityPoint(int activityPoint) {
		this.activityPoint = activityPoint;
	}
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getAdjResion() {
		return adjResion;
	}
	public void setAdjResion(String adjResion) {
		this.adjResion = adjResion;
	}
	public int getExtPointRuleId() {
		return extPointRuleId;
	}
	public void setExtPointRuleId(int extPointRuleId) {
		this.extPointRuleId = extPointRuleId;
	}
	public int getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(int extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
	}
	public String getSetTime() {
		return setTime;
	}
	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}
	public String getPointHistoryId() {
		return pointHistoryId;
	}
	public void setPointHistoryId(String pointHistoryId) {
		this.pointHistoryId = pointHistoryId;
	}
	public int getOrgPoint() {
		return orgPoint;
	}
	public void setOrgPoint(int orgPoint) {
		this.orgPoint = orgPoint;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public int getPointBlance() {
		return pointBlance;
	}
	public void setPointBlance(int pointBlance) {
		this.pointBlance = pointBlance;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
}
