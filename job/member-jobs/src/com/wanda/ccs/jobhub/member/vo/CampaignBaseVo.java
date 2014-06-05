/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

/**
 * 活动基本信息
 * 
 * @author ZR
 * 
 */
public class CampaignBaseVo implements Serializable {

	private static final long serialVersionUID = -2701531605581714528L;
	/* 活动ID */
	protected Long campaignId;
	/* 推荐规则 */
	protected String criteriaScheme;
	/* 影城规则 */
	protected String cinemaScheme;
	/* 目标人数 */
	protected Integer calCount;
	/* 对比组数量 */
	protected Integer controlCount;
	/* 推荐响应人数 */
	protected Integer recommendCount;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCriteriaScheme() {
		return criteriaScheme;
	}

	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}

	public String getCinemaScheme() {
		return cinemaScheme;
	}

	public void setCinemaScheme(String cinemaScheme) {
		this.cinemaScheme = cinemaScheme;
	}

	public Integer getCalCount() {
		return calCount;
	}

	public void setCalCount(Integer calCount) {
		this.calCount = calCount;
	}

	public Integer getControlCount() {
		return controlCount;
	}

	public void setControlCount(Integer controlCount) {
		this.controlCount = controlCount;
	}

	public Integer getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(Integer recommendCount) {
		this.recommendCount = recommendCount;
	}

}
