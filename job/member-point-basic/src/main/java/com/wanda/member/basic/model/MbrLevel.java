package com.wanda.member.basic.model;

public class MbrLevel {
	private String memberId;
	private int levelPointTotal;
	private int ticketCount;
	private int memLevel;
	private String year;
	
	private String levelHisId;
	
	public int getDegradeLv(){
		int degradeLv = this.memLevel - 1;
		return degradeLv > 0 ? degradeLv : 1;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getLevelPointTotal() {
		return levelPointTotal;
	}
	public void setLevelPointTotal(int levelPointTotal) {
		this.levelPointTotal = levelPointTotal;
	}
	public int getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}
	public int getMemLevel() {
		return memLevel;
	}
	public void setMemLevel(int memLevel) {
		this.memLevel = memLevel;
	}
	public String getLevelHisId() {
		return levelHisId;
	}
	public void setLevelHisId(String levelHisId) {
		this.levelHisId = levelHisId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
