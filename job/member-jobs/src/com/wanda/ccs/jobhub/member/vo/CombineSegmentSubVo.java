package com.wanda.ccs.jobhub.member.vo;

import java.sql.Timestamp;

public class CombineSegmentSubVo {
	private Long combineSegmentSubId;
	private Long segmentId;
	private Long subSegmentId;
	private Long sortIndex;//排序索引
	private String setRelation;//复合集合关系
	private Long countAlter;//复合后数据变化
	private Long calCount;//实际数量
	private Timestamp calCountTime;//实际数量计算时间
	private String segmentVersion;
	
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
	public Long getSortIndex() {
		return sortIndex;
	}
	public void setSortIndex(Long sortIndex) {
		this.sortIndex = sortIndex;
	}
	public String getSetRelation() {
		return setRelation;
	}
	public void setSetRelation(String setRelation) {
		this.setRelation = setRelation;
	}
	public Long getCountAlter() {
		return countAlter;
	}
	public void setCountAlter(Long countAlter) {
		this.countAlter = countAlter;
	}
	public Long getCalCount() {
		return calCount;
	}
	public void setCalCount(Long calCount) {
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
	
	
}
