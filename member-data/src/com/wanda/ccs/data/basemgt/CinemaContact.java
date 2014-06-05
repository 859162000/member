package com.wanda.ccs.data.basemgt;

import java.util.Date;

import com.icebean.core.adb.ADB;

public class CinemaContact extends ADB {
	private long seqId;
	private long cinema_Id;
	private String name;
	private String phone;
	private String email;
	private String duty;
	private String address;
	private String post_Code;
	private int isDelete;
	private Date update_Time;
	private int sex;
	private String mobile;
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	public long getCinema_Id() {
		return cinema_Id;
	}
	public void setCinema_Id(long cinema_Id) {
		this.cinema_Id = cinema_Id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPost_Code() {
		return post_Code;
	}
	public void setPost_Code(String post_Code) {
		this.post_Code = post_Code;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public Date getUpdate_Time() {
		return update_Time;
	}
	public void setUpdate_Time(Date update_Time) {
		this.update_Time = update_Time;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
