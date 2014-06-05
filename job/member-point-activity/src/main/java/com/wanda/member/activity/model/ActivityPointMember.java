package com.wanda.member.activity.model;

public class ActivityPointMember {
	private String memberKey;
	private int pointAdditionCode;
	private int extPointRuleId;
    private int extPointCriteriaId;
    private String bizDate;
	public String getMemberKey() {
		return memberKey;
	}

	public void setMemberKey(String memberKey) {
		this.memberKey = memberKey;
	}

	public int getPointAdditionCode() {
		return pointAdditionCode;
	}

	public void setPointAdditionCode(int pointAdditionCode) {
		this.pointAdditionCode = pointAdditionCode;
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

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	
}
