/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.sql.Date;

/**
 * 活动基本信息
 * 
 * @author ZR
 * 
 */
public class CampaignVo extends CampaignBaseVo {

	private static final long serialVersionUID = -2701531605581714528L;

	public static final String STATUS_PREPARE = "10";
	public static final String STATUS_COMMIT = "20";
	public static final String STATUS_EXECUTION = "30";
	public static final String STATUS_COMPLETE = "40";

	protected Date startDate;
	protected Date endDate;
	protected String campaignStatus;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

}
