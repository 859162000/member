package com.wanda.ccs.data.schedule;

import java.sql.Date;
import java.util.Set;

import com.icebean.core.adb.ADB;

/**
 * 排片信息表头
 * 
 * @author Danne Leung
 */
public class HScheduleInfo extends ADB {
	public static final int MAX_VERSION = 3;
	private long seqId;
	private boolean approved;
	private String approvedBy;
	private Date approvedTime;
	private String createBy;
	private Date createTime;
	private Set<BScheduleInfo> list;
	private String month;
	private String status;
	private boolean submit;
	private String submitBy;
	private Date submitTime;
	private String updateBy;
	private Date updateTime;
	private String version;

	public HScheduleInfo() {
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public Date getApprovedTime() {
		return approvedTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Set<BScheduleInfo> getList() {
		return list;
	}

	public String getMonth() {
		return month;
	}

	public long getSeqId() {
		return seqId;
	}

	public String getStatus() {
		return status;
	}

	public String getSubmitBy() {
		return submitBy;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getVersion() {
		return version;
	}

	public boolean getApproved() {
		return approved;
	}

	public boolean isApproved() {
		return approved;
	}

	public boolean getSubmit() {
		return submit;
	}

	public boolean isSubmit() {
		return submit;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public void setApprovedTime(Date dtApprovedDate) {
		this.approvedTime = dtApprovedDate;
	}

	public void setCreateBy(String createdBy) {
		this.createBy = createdBy;
	}

	public void setCreateTime(Date createdDate) {
		this.createTime = createdDate;
	}

	public void setList(Set<BScheduleInfo> list) {
		this.list = list;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public void setStatus(String status) {
		if (status == null || status.trim().length() == 0) {
			clearFlag("status");
		} else {
			setFlag("status");
		}
		this.status = status;
	}

	public void setSubmit(boolean submit) {
		this.submit = submit;
	}

	public void setSubmitBy(String submitBy) {
		this.submitBy = submitBy;
	}

	public void setSubmitTime(Date dtSubmitDate) {
		this.submitTime = dtSubmitDate;
	}

	public void setUpdateBy(String updatedBy) {
		this.updateBy = updatedBy;
	}

	public void setUpdateTime(Date updateDate) {
		this.updateTime = updateDate;
	}

	public void setVersion(String version) {
		if (version == null || version.trim().length() == 0) {
			clearFlag("version");
		} else {
			setFlag("version");
		}
		this.version = version;
	}

}
