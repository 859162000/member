package com.wanda.mms.control.stream.vo;

import java.io.Serializable;

/*
 * Copyright (c) 2007-2009 talkWeb All rights reserved.
 * History: 
 * Date          Modified_By    Reason    Description 
 * 2011-01-04    xiaowenbin       锟斤拷锟斤拷      锟斤拷锟斤拷
 */


/**
 * 
 * 
 * @author xiaowenbin
 * 
 */

public class Sms implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6912965234793378370L;

	private String smsId;

	private String mobileNo;// 

	private String mtAddr;

	private String moAddr;

	private String mt;

	private String mo;//　

	private java.util.Date mtTime;

	private java.util.Date moTime;

	private int reSendNum;

	private String sendState;

	private java.util.Date submitTime;
 
	
	public String toXML() {
		return "<Sms smsId=\""
				+ smsId
				+ "\"  mobileNo=\""
				+ mobileNo
				+ "\" mtAddr=\""
				+ mtAddr
				+ "\" moAddr=\""
				+ moAddr
				+ "\" mt=\""
				+ mt
				+ "\" mo=\""
				+ mo
				+ "\" mtTime=\""
				+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(mtTime == null ? new java.util.Date() : mtTime)
				+ "\" moTime=\""
				+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(moTime == null ? new java.util.Date() : moTime)
				+ "\" reSendNum=\"" + reSendNum + "\" sendState=\"" + sendState
				+ "\"/>";
	}
	
	public String getMo() {
		return mo;
	}

	public void setMo(String mo) {
		this.mo = mo;
	}

	public String getMoAddr() {
		return moAddr;
	}

	public void setMoAddr(String moAddr) {
		this.moAddr = moAddr;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public java.util.Date getMoTime() {
		return moTime;
	}

	public void setMoTime(java.util.Date moTime) {
		this.moTime = moTime;
	}

	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}

	public String getMtAddr() {
		return mtAddr;
	}

	public void setMtAddr(String mtAddr) {
		this.mtAddr = mtAddr;
	}

	public java.util.Date getMtTime() {
		return mtTime;
	}

	public void setMtTime(java.util.Date mtTime) {
		this.mtTime = mtTime;
	}

	public int getReSendNum() {
		return reSendNum;
	}

	public void setReSendNum(int reSendNum) {
		this.reSendNum = reSendNum;
	}

	public String getSendState() {
		return sendState;
	}

	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public java.util.Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(java.util.Date submitTime) {
		this.submitTime = submitTime;
	}

}
