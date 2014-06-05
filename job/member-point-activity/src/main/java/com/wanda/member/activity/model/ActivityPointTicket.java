package com.wanda.member.activity.model;

import java.math.BigDecimal;

public class ActivityPointTicket {
	private String innerCode;
	private String memberKey;
	private String ctOrderCode;
	private String ticketNumber;
	private BigDecimal admissions;
	private int pointPercent;
	private int extPointRuleId;
    private int extPointCriteriaId;
	private String bizDate;
	private int pointAddition;
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public String getMemberKey() {
		return memberKey;
	}
	public void setMemberKey(String memberKey) {
		this.memberKey = memberKey;
	}
	public String getCtOrderCode() {
		return ctOrderCode;
	}
	public void setCtOrderCode(String ctOrderCode) {
		this.ctOrderCode = ctOrderCode;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public BigDecimal getAdmissions() {
		return admissions;
	}
	public void setAdmissions(BigDecimal admissions) {
		this.admissions = admissions;
	}
	public int getPointPercent() {
		return pointPercent;
	}
	public void setPointPercent(int pointPercent) {
		this.pointPercent = pointPercent;
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
	public int getPointAddition() {
		return pointAddition;
	}
	public void setPointAddition(int pointAddition) {
		this.pointAddition = pointAddition;
	}
}
