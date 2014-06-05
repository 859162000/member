/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

/**
 * @author YangJianbin
 * 
 */
public class TargetVo implements Serializable {
	private static final long serialVersionUID = 222683077927945522L;
	private Long actTargetId;
	private Long segmentId;//客群
	private String targetType;//波次目标类型-DIM1012
	private Long totalCount;//总数(客群总数、导入总数）
	private Long maxCount;//受众数量
	private Long controlCount;//控制组数量(需要小于客群数量)
	private Long cmnActivityId;//活动波次
	private Long fileAttachId;//文件id



	public TargetVo() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getActTargetId() {
		return actTargetId;
	}

	public void setActTargetId(Long actTargetId) {
		this.actTargetId = actTargetId;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}

	public Long getControlCount() {
		return controlCount;
	}

	public void setControlCount(Long controlCount) {
		this.controlCount = controlCount;
	}
	
	public Long getCmnActivityId() {
		return cmnActivityId;
	}

	public void setCmnActivityId(Long cmnActivityId) {
		this.cmnActivityId = cmnActivityId;
	}
	
	public Long getFileAttachId() {
		return fileAttachId;
	}

	public void setFileAttachId(Long fileAttachId) {
		this.fileAttachId = fileAttachId;
	}
	
	public String toString() {
		return actTargetId+"|"+targetType;
	}
}
