package com.wanda.member.activity.model;

import java.math.BigDecimal;

public class ActivityPointGoods {
	private String innerCode;
	private String memberKey;
	private String csOrderCode;
	private String itemCode;
	private BigDecimal saleAmount;
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
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getCsOrderCode() {
		return csOrderCode;
	}
	public void setCsOrderCode(String csOrderCode) {
		this.csOrderCode = csOrderCode;
	}
	public BigDecimal getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
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
