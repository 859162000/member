package com.wanda.ccs.member.segment.vo;

import java.sql.Timestamp;

/**
 * 活动
 * 
 * @author zhurui
 * @date 2014年3月25日 上午11:43:50
 */
public class CampaignCriteriaVo extends BaseAuditVo {
	private static final long serialVersionUID = 451896339250572185L;

	public final static String STATUS_PREPARE = "10"; // 草稿
	public final static String STATUS_COMMIT = "20"; // 提交
	public final static String STATUS_CALCULATING = "30"; // 执行
	public final static String STATUS_COMPLETE = "40"; // 结束

	/* 活动ID */
	private Long campaignId;
	/* 活动名称 */
	private String name;
	/* 活动编码 */
	private String code;
	/* 影城范围 */
	private String cinemaRange;
	/* 开始时间 */
	private String startDate;
	/* 结束时间 */
	private String endDate;
	/* 状态 */
	private String status;
	/* 描述 */
	private String description;
	/* 推荐响应规则 */
	private String criteriaScheme;
	/* 影城范围 */
	private String cinemaScheme;
	
	private String createBy;
	private Timestamp createDate;
	private String updateBy;
	private Timestamp updateDate;
	
	/* 客群ID */
	private Long segmentId;
	/* 客群名称 */
	private String segmentName;
	/* 客群数量 */
	private String calCount;
	/* 客群计算时间 */
	private Timestamp calCountTime;
	/* 对比组数量 */
	private String controlCount;
	/* 客群版本 */
	private String segmentVersion;
	
	/* 创建者所属级别:GROUP:院线,REGION:区域;CINEMA:影城 */
	private String ownerLevel;
	/* 创建者所属区域:区域代码,dim104 */
	private String ownerRegion;
	/* 创建者所属影城:影城seqid */
	private Long ownerCinema;

	public CampaignCriteriaVo() {}
	
	public CampaignCriteriaVo(Long campaignId, String name, String code,
			String cinemaRange, String createBy, Timestamp createDate,
			String updateBy, Timestamp updateDate, String startDate,
			String endDate, String status, String description,
			Long segmentId, String segmentName) {
		this.campaignId = campaignId;
		this.name = name;
		this.code = code;
		this.cinemaRange = cinemaRange;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.description = description;
		this.segmentId = segmentId;
		this.segmentName=segmentName;
	}

	public String getOwnerLevel() {
		return ownerLevel;
	}

	public void setOwnerLevel(String ownerLevel) {
		this.ownerLevel = ownerLevel;
	}

	public String getOwnerRegion() {
		return ownerRegion;
	}

	public void setOwnerRegion(String ownerRegion) {
		this.ownerRegion = ownerRegion;
	}

	public Long getOwnerCinema() {
		return ownerCinema;
	}

	public void setOwnerCinema(Long ownerCinema) {
		this.ownerCinema = ownerCinema;
	}

	public String retrieveCode() {
		return getCode();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCriteriaScheme() {
		return criteriaScheme;
	}

	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	public String getCalCount() {
		return calCount;
	}

	public void setCalCount(String calCount) {
		this.calCount = calCount;
	}

	public Timestamp getCalCountTime() {
		return calCountTime;
	}

	public void setCalCountTime(Timestamp calCountTime) {
		this.calCountTime = calCountTime;
	}

	public String getSegmentVersion() {
		return segmentVersion;
	}

	public void setSegmentVersion(String segmentVersion) {
		this.segmentVersion = segmentVersion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCinemaRange() {
		return cinemaRange;
	}

	public void setCinemaRange(String cinemaRange) {
		this.cinemaRange = cinemaRange;
	}
	
	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getCinemaScheme() {
		return cinemaScheme;
	}

	public void setCinemaScheme(String cinemaScheme) {
		this.cinemaScheme = cinemaScheme;
	}

	public String getControlCount() {
		return controlCount;
	}

	public void setControlCount(String controlCount) {
		this.controlCount = controlCount;
	}

	@Override
	public Long retrieveSeqId() {
		return getCampaignId();
	}

}
