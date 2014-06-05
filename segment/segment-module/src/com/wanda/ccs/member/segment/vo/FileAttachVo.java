package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;

public class FileAttachVo implements Serializable  {

	private static final long serialVersionUID = -6102222929479806487L;
	
	private Long fileAttachId;
	
	private Long refObjectId;
	
	private String refObjectType;
	
	private String fileName;
	
	private Long fileSize;
	
	private String fileDesc;
	
	private String contentType;

	public Long getFileAttachId() {
		return fileAttachId;
	}

	public void setFileAttachId(Long fileAttachId) {
		this.fileAttachId = fileAttachId;
	}

	public Long getRefObjectId() {
		return refObjectId;
	}

	public void setRefObjectId(Long refObjectId) {
		this.refObjectId = refObjectId;
	}

	public String getRefObjectType() {
		return refObjectType;
	}

	public void setRefObjectType(String refObjectType) {
		this.refObjectType = refObjectType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
