package com.wanda.ccs.jobhub.smsreport.vo;

import java.io.Serializable;

/**
 * 人员信息
 * 
 * @author zhurui
 * @date 2014年1月7日 下午3:43:15
 */
public class Target implements Serializable {

	private static final long serialVersionUID = 1L;
	/* ID */
	private Long id;
	/* 名字 */
	private String name;
	/* 岗位 */
	private String duty;
	/* 移动电话 */
	private String mobile;
	/* RTX */
	private String rtx;
	/* 是否系统运维人员 0-否（缺省） 1-是 */
	private int IsSystemUser;
	/* 影城内码 */
	private String cinemaInnerCode;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRtx() {
		return rtx;
	}

	public void setRtx(String rtx) {
		this.rtx = rtx;
	}

	public int getIsSystemUser() {
		return IsSystemUser;
	}

	public void setIsSystemUser(int isSystemUser) {
		IsSystemUser = isSystemUser;
	}

	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}

	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}

}
