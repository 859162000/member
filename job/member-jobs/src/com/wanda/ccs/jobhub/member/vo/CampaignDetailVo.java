/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.sql.Timestamp;

/**
 * 活动明细
 * 
 * @author ZR
 * 
 */
public class CampaignDetailVo extends CampaignVo {

	public static final String STATUS_CREATE = "10";
	public static final String STATUS_SUCCESS = "20";
	public static final String STATUS_FAIL = "30";
	
	private static final long serialVersionUID = -6510840224667071489L;
	/*  */
	private Long id;
	/* 统计日期 */
	private Integer ymd;
	/* 推荐响应人数 */
	private Integer recommendNum;
	/* 控制组响应人数 */
	private Integer controlNum;
	/* 总人数 */
	private Integer sumNum;
	/* 推荐响应率 */
	private double recommendRate;
	/* 控制组响应率 */
	private double controlRate;
	/* 总人数率 */
	private double sumRate;
	/* 推荐响应收入 */
	private double recommendIncome;
	/* 控制组响应收入 */
	private double controlIncome;
	/* 总收入 */
	private double sumIncome;
	/* 创建时间 */
	private Timestamp createDate;
	/* 类型 */
	private String type = "0";
	/* 更新时间 */
	private Timestamp updateDate;
	/* 执行状态 */
	private String status = "10";
	
	public CampaignDetailVo() {}
	
	public CampaignDetailVo(Long campaignId, Integer ymd, String type) {
		this.campaignId = campaignId;
		this.ymd = ymd;
		this.type = type;
	}
	
	public CampaignDetailVo(CampaignVo vo) {
		setCampaignVo(vo);
	}
	
	public void setCampaignVo(CampaignVo vo) {
		this.campaignId = vo.getCampaignId();
		this.criteriaScheme = vo.getCriteriaScheme();
		this.cinemaScheme = vo.getCinemaScheme();
		this.calCount = vo.getCalCount();
		this.controlCount = vo.getControlCount();
		this.recommendCount = vo.getRecommendCount();
		this.startDate = vo.getStartDate();
		this.endDate = vo.getEndDate();
		this.campaignStatus = vo.getCampaignStatus();
	}

	public String getDateFormat() {
		String t = ymd.toString();
		return t.substring(0, 4) + "-" + t.substring(4, 6) + "-"
				+ t.substring(6, 8);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYmd() {
		return ymd;
	}

	public void setYmd(Integer ymd) {
		this.ymd = ymd;
	}

	public Integer getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(Integer recommendNum) {
		this.recommendNum = recommendNum;
	}

	public Integer getControlNum() {
		return controlNum;
	}

	public void setControlNum(Integer controlNum) {
		this.controlNum = controlNum;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public double getRecommendRate() {
		return recommendRate;
	}

	public void setRecommendRate(double recommendRate) {
		this.recommendRate = recommendRate;
	}

	public double getControlRate() {
		return controlRate;
	}

	public void setControlRate(double controlRate) {
		this.controlRate = controlRate;
	}

	public double getSumRate() {
		return sumRate;
	}

	public void setSumRate(double sumRate) {
		this.sumRate = sumRate;
	}

	public double getRecommendIncome() {
		return recommendIncome;
	}

	public void setRecommendIncome(double recommendIncome) {
		this.recommendIncome = recommendIncome;
	}

	public double getControlIncome() {
		return controlIncome;
	}

	public void setControlIncome(double controlIncome) {
		this.controlIncome = controlIncome;
	}

	public double getSumIncome() {
		return sumIncome;
	}

	public void setSumIncome(double sumIncome) {
		this.sumIncome = sumIncome;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
