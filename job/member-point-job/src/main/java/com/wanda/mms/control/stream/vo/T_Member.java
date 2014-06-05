package com.wanda.mms.control.stream.vo;
/**
 *	会员表
 * @author wangshuai
 * @date 2013-05-28	
 */
public class T_Member {
	
	private Long memberId;
	private String channelId;// 'BO:影城','POS:POS','WEB:电影网','Mobile:移动终端','SinaWeibo:合作渠道'
	private String memberNo;// 会员序列号
	private String mobile;// 会员唯一标识，非空，不允许重复
	private String phone;
	private String email;
	private String name;// 会员名称
	private String gender;// M:男,F:女
	private String birthday;// 会员生日
	private Long registType;// 注册类型 1:新会员;2:卡转换;3:合作伙伴
	private String registOpNo;// 注册号
	private String registOpName;// 注册名
	private Long registCinemaId;// 影院全国长码
	private Long status;// 1:有效;0:无效
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
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
	public Long getRegistCinemaId() {
		return registCinemaId;
	}
	public void setRegistCinemaId(Long registCinemaId) {
		this.registCinemaId = registCinemaId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}

	
}
