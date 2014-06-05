package com.wanda.member.basic.model;

import java.math.BigDecimal;

public class BasicPointTrans {
	private String innerCode;
	private String memberId;
	private String orderCode;
	private String itemCode;
	private BigDecimal amount;
	private String bizDate;
	private String transType;
	private int levelPoint;
	private int orgPoint;
	private String pointHistoryId;
	private String adjResion;
	private int pointBlance;
	
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public int getLevelPoint() {
		return levelPoint;
	}
	public void setLevelPoint(int levelPoint) {
		this.levelPoint = levelPoint;
	}
	public int getOrgPoint() {
		return orgPoint;
	}
	public void setOrgPoint(int orgPoint) {
		this.orgPoint = orgPoint;
	}
	public String getPointHistoryId() {
		return pointHistoryId;
	}
	public void setPointHistoryId(String pointHistoryId) {
		this.pointHistoryId = pointHistoryId;
	}
	public String getAdjResion() {
		return adjResion;
	}
	public void setAdjResion(String adjResion) {
		this.adjResion = adjResion;
	}
	public int getPointBlance() {
		return pointBlance;
	}
	public void setPointBlance(int pointBlance) {
		this.pointBlance = pointBlance;
	}
	
}
