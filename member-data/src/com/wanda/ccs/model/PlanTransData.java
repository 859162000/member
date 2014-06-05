package com.wanda.ccs.model;

// Generated 2011-10-26 10:19:09 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

public class PlanTransData {

	public PlanTransData() {
	}

	private Boolean broadcast;
	private String tranSts;
	private String tranFn;
	private String tranMsg;
	private Date tranTime;

	public PlanTransData(Boolean broadcast, String tranSts, String tranFn,
			String tranMsg, Date tranTime) {
		super();
		this.broadcast = broadcast;
		this.tranSts = tranSts;
		this.tranFn = tranFn;
		this.tranMsg = tranMsg;
		this.tranTime = tranTime;
	}

	public boolean isTakedByPOS(){
		return "0".equalsIgnoreCase(this.getTranSts());
	}
	public boolean isArrivedDET(){
		return "1".equalsIgnoreCase(this.getTranSts());
	}
	public boolean isGoingToDET(){
		return "2".equalsIgnoreCase(this.getTranSts());
	}
	public Boolean getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}

	public String getTranSts() {
		return tranSts;
	}

	public void setTranSts(String tranSts) {
		this.tranSts = tranSts;
	}

	public String getTranFn() {
		return tranFn;
	}

	public void setTranFn(String tranFn) {
		this.tranFn = tranFn;
	}

	public String getTranMsg() {
		return tranMsg;
	}

	public void setTranMsg(String tranMsg) {
		this.tranMsg = tranMsg;
	}

	public Date getTranTime() {
		return tranTime;
	}

	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}

	@Override
	public String toString() {
		return this.getBroadcast() + this.getTranFn() + this.getTranMsg()
				+ this.getTranSts() + this.getTranTime();
	}
}
