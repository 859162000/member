package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;

public class DataAuditVo implements Serializable {

	private static final long serialVersionUID = 3688891424934033988L;
	
	private Long dataAuditId;
	
	private String dataType;
	
	private Long dataSeqId;
	
	private String dataCode;
	
	private String dataContent;
	
	private String actionType;
	
	private String actionName;
	
	private String actionUserId;
	
	private java.sql.Timestamp actionDate;
	
	private Long version;
	
	public enum ActionType {
		INSERT, UPDATE, LOGIC_DELETE, DELETE
	}

	public Long getDataAuditId() {
		return dataAuditId;
	}

	public void setDataAuditId(Long dataAuditId) {
		this.dataAuditId = dataAuditId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getDataSeqId() {
		return dataSeqId;
	}

	public void setDataSeqId(Long dataSeqId) {
		this.dataSeqId = dataSeqId;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(String actionUserId) {
		this.actionUserId = actionUserId;
	}

	public java.sql.Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(java.sql.Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	
}
