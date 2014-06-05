package com.wanda.mms.control.stream.vo;



/**
 *  
 *个人信息实体类
 * @author wangshuai 
 * @date 2013-04-15
 */
public class Target {
	/**
	 * 个人信息ID
	 */
	private Long targetId;
	/**
	 * 个人信息名字
	 */
	private String name;
	/**
	 * 岗位
	 */
	private String duty;
	/**
	 * 移动电话
	 */
	private String mobile;
	/**
	 * RTX号
	 */
	private String rtx;
	/**
	 * 是否系统运维人员
	 * 0-否（缺省）
	 * 1-是
	 */
	private int IsSystemUser;
	
	private String cinema_inner_code;//影城内码
	
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
	public String getCinema_inner_code() {
		return cinema_inner_code;
	}
	public void setCinema_inner_code(String cinemaInnerCode) {
		cinema_inner_code = cinemaInnerCode;
	}
	
	
	

}
