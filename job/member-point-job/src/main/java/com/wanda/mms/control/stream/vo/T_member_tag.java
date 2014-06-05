package com.wanda.mms.control.stream.vo;

public class T_member_tag {
	/**
	 *会员ID#MEMBER_ID
	 */
	public long member_id;
	/**
	 *首映零点场观影#FIRST_SHOW
	 */
	public String first_show;
	/**
	 *价格敏感#PRICE_SENSE
	 */
	public String price_sense;
	/**
	 *家庭构成#FAMILY_STRUCT
	 */
	public String family_struct;
	/**
	 *异常消费#EXCEPTION_TRANS
	 */
	public String exception_trans;
	/**
	 *会员活跃度#ACTIVITY
	 */
	public String activity;
	/**
	 *逻辑删除标识#ISDELETE
	 */
	private int isdelete;
	/**
	 *创建者#CREATE_BY
	 */
	private String create_By;
	/**
	 *创建时间#CREATE_DATE
	 */
	private String create_Date;
	/**
	 *更新者#UPDATE_BY
	 */
	private String update_By;
	/**
	 *更新时间#UPDATE_DATE
	 */
	private String update_Date;
	/**
	 *版本号#VERSION
	 */
	private int version;
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long memberId) {
		member_id = memberId;
	}
	public String getFirst_show() {
		return first_show;
	}
	public void setFirst_show(String firstShow) {
		first_show = firstShow;
	}
	public String getPrice_sense() {
		return price_sense;
	}
	public void setPrice_sense(String priceSense) {
		price_sense = priceSense;
	}
	public String getFamily_struct() {
		return family_struct;
	}
	public void setFamily_struct(String familyStruct) {
		family_struct = familyStruct;
	}
	public String getException_trans() {
		return exception_trans;
	}
	public void setException_trans(String exceptionTrans) {
		exception_trans = exceptionTrans;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public String getCreate_By() {
		return create_By;
	}
	public void setCreate_By(String createBy) {
		create_By = createBy;
	}
	public String getCreate_Date() {
		return create_Date;
	}
	public void setCreate_Date(String createDate) {
		create_Date = createDate;
	}
	public String getUpdate_By() {
		return update_By;
	}
	public void setUpdate_By(String updateBy) {
		update_By = updateBy;
	}
	public String getUpdate_Date() {
		return update_Date;
	}
	public void setUpdate_Date(String updateDate) {
		update_Date = updateDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
