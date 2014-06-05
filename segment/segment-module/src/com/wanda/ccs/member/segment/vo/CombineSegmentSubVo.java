package com.wanda.ccs.member.segment.vo;

import java.sql.Timestamp;

/**
 * 子客群
 * 
 * @author zhurui
 * @date 2013年12月17日 下午6:00:24
 */
public class CombineSegmentSubVo extends BaseAuditVo {

	private static final long serialVersionUID = 6473947773839474425L;
	/* 主键 */
	private Long combineSegmentSubId;
	/* 所属主客群ID */
	private Long segmentId;
	/* 子客群ID */
	private Long subSegmentId;
	/* 操作顺序 */
	private Integer sortIndex;
	/* 操作类型 */
	private String setRelation;
	/* 值差 */
	private Long countAlter = -1L;
	/* 子客群编码 */
	private String code;
	/* 子客群名称 */
	private String name;
	/* 子客群计算数量 */
	private Long calCount = -1L;
	/* 咨客群计算生成时间 */
	private Timestamp calCountTime;
	private Long controlCount = -1L;
	/* 子客群版本 */
	private Long segmentVersion;

	/* 下面为复合客群中子客群对应的客群数据 */
	/* 客群状态 */
	private String status;
	/* 客群计算数量 */
	private Integer segmentCalCount;
	/* 客群计算时间 */
	private Timestamp segmentCalCountTime;

	/* 此字段不对应数据库中字段，主要用来当编辑操作后表明当前这个子客群是new新加的，edit修改的，delete需要删除的 */
	private String controlStatus;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public java.sql.Timestamp getCalCountTime() {
		return calCountTime;
	}

	public void setCalCountTime(java.sql.Timestamp calCountTime) {
		this.calCountTime = calCountTime;
	}

	@Override
	public String retrieveCode() {
		return null;
	}

	@Override
	public Long retrieveSeqId() {
		return getCombineSegmentSubId();
	}

	public String getControlStatus() {
		return controlStatus;
	}

	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
	}

	public Long getSegmentVersion() {
		return segmentVersion;
	}

	public void setSegmentVersion(Long segmentVersion) {
		this.segmentVersion = segmentVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSegmentCalCount() {
		return segmentCalCount;
	}

	public void setSegmentCalCount(Integer segmentCalCount) {
		this.segmentCalCount = segmentCalCount;
	}

	public Timestamp getSegmentCalCountTime() {
		return segmentCalCountTime;
	}

	public void setSegmentCalCountTime(Timestamp segmentCalCountTime) {
		this.segmentCalCountTime = segmentCalCountTime;
	}

	public Long getControlCount() {
		return controlCount;
	}

	public void setControlCount(Long controlCount) {
		this.controlCount = controlCount;
	}

}
