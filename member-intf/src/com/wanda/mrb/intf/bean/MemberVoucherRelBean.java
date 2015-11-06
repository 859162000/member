package com.wanda.mrb.intf.bean;

public class MemberVoucherRelBean {
	private String voucherNumber;
	private String voucherTypeCode;
	private String voucherTypeCodeName;
	private String startDate;
	private String endDate;
	private String useType;
	private String operateType;
	private String unitValue;
	private String mimPrice;
	
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getUnitValue() {
		return unitValue;
	}
	public void setUnitValue(String unitValue) {
		this.unitValue = unitValue;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	//	 p.ENCODE_PRINT_CODE,o.SALE_DATE,o.EXPIRY_DATE,t.TYPE_CODE,t.TYPE_NAME
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	
	public String getVoucherTypeCode() {
		return voucherTypeCode;
	}
	public void setVoucherTypeCode(String voucherTypeCode) {
		this.voucherTypeCode = voucherTypeCode;
	}
	public String getVoucherTypeCodeName() {
		return voucherTypeCodeName;
	}
	public void setVoucherTypeCodeName(String voucherTypeCodeName) {
		this.voucherTypeCodeName = voucherTypeCodeName;
	}
	public String getMimPrice() {
		return mimPrice;
	}
	public void setMimPrice(String mimPrice) {
		this.mimPrice = mimPrice;
	}
	
}
