package com.wanda.ccs.jobhub.member.vo;

import java.util.Date;

public class MemberBean {
	private Long memberId;
	private String memberNo;// 会员序列号
	private String memberName;
	private String mobile;
	private String email;//
	private String gender;//性别
	private Date birthDate;//生日
	private Long cinemaId;//注册影城
	private String innerCode;//影城内码管理影城
    private Long contactable ;//是否希望被联系
    private String address;//住址
    private Long privence;//省
    private Long city;//城市
    private String changeStatusResion;//会员调整状态原因
	private Long isDeleted;//是否删除
	private Long status;// 1:有效;0:无效
	private Long registType;// 注册类型 1:新会员;2:卡转换;3:合作伙伴
	private String registOpNo;// 注册号
	private String registOpName;// 注册名
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Long getCinemaId() {
		return cinemaId;
	}
	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public Long getContactable() {
		return contactable;
	}
	public void setContactable(Long contactable) {
		this.contactable = contactable;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getPrivence() {
		return privence;
	}
	public void setPrivence(Long privence) {
		this.privence = privence;
	}
	public Long getCity() {
		return city;
	}
	public void setCity(Long city) {
		this.city = city;
	}
	public String getChangeStatusResion() {
		return changeStatusResion;
	}
	public void setChangeStatusResion(String changeStatusResion) {
		this.changeStatusResion = changeStatusResion;
	}
	public Long getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getRegistType() {
		return registType;
	}
	public void setRegistType(Long registType) {
		this.registType = registType;
	}
	public String getRegistOpNo() {
		return registOpNo;
	}
	public void setRegistOpNo(String registOpNo) {
		this.registOpNo = registOpNo;
	}
	public String getRegistOpName() {
		return registOpName;
	}
	public void setRegistOpName(String registOpName) {
		this.registOpName = registOpName;
	}
	
	
	
}
