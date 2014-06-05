package com.wanda.ccs.jobhub.member.vo;

import java.sql.Timestamp;

/**
 * 子客群
 * 
 * @author zhurui
 * @date 2013年12月24日 上午10:53:32
 */
public class CombineSegmentSubVo {
	/* 子客群ID */
	private Long combineSegmentSubId;
	/* 客群ID */
	private Long segmentId;
	/* 子客群ID */
	private Long subSegmentId;
	/* 排序 */
	private Integer sortIndex;
	/* 复合关系 */
	private String setRelation;
	/* 复合后数据变化 */
	private Integer countAlter;
	/* 当前子客群实际数量 */
	private Integer subCalCount;
	/* 当前子客群实际数量计算时间 */
	private Timestamp subCalCountTime;
	/* 当前子客群版本号 */
	private String segmentVersion;

	/* 查询条件 */
	private String criteriaScheme;
	/*  */
	private String sortName;
	/*  */
	private String sortOrder;
	/* 最大数据量 */
	private Long maxCount;
	/* 客群实际数量 */
	private Integer calCount;
	/* 客群实际数量计算时间 */
	private Timestamp calCountTime;
	/* 客群状态 */
	private String status;
	/* 客群版本号 */
	private String version;

	public Long getCombineSegmentSubId() {
		return combineSegmentSubId;
	}

	public void setCombineSegmentSubId(Long combineSegmentSubId) {
		this.combineSegmentSubId = combineSegmentSubId;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	public Long getSubSegmentId() {
		return subSegmentId;
	}

	public void setSubSegmentId(Long subSegmentId) {
		this.subSegmentId = subSegmentId;
	}
	

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getSetRelation() {
		return setRelation;
	}

	public void setSetRelation(String setRelation) {
		this.setRelation = setRelation;
	}

	public Integer getCalCount() {
		return calCount;
	}

	public void setCalCount(Integer calCount) {
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
	
	

	public Integer getCountAlter() {
		return countAlter;
	}

	public void setCountAlter(Integer countAlter) {
		this.countAlter = countAlter;
	}

	public Integer getSubCalCount() {
		return subCalCount;
	}

	public void setSubCalCount(Integer subCalCount) {
		this.subCalCount = subCalCount;
	}

	public Timestamp getSubCalCountTime() {
		return subCalCountTime;
	}

	public void setSubCalCountTime(Timestamp subCalCountTime) {
		this.subCalCountTime = subCalCountTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCriteriaScheme() {
		return criteriaScheme;
	}

	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
