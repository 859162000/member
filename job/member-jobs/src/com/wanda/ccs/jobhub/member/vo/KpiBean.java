package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

public class KpiBean implements Serializable{
	
	private static final long serialVersionUID = -4755998812815935033L;

	public KpiBean() {
		
	}

	/**
	 * 导入年份
	 */
	private String kpiYear;
	
	/**
	 * 导入影城（影城ID）
	 */
	private Long cinemaId;
	
	/**
	 * 导入数量
	 */
	private double kpiValue;
	
	private String kpiName;
	private String kpiType;
	private String innerCode;

	public String getKpiYear() {
		return kpiYear;
	}

	public void setKpiYear(String kpiYear) {
		this.kpiYear = kpiYear;
	}

	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public double getKpiValue() {
		return kpiValue;
	}

	public void setKpiValue(double kpiValue) {
		this.kpiValue = kpiValue;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiType() {
		return kpiType;
	}

	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

}
