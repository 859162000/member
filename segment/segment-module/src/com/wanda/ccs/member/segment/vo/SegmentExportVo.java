package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;


public class SegmentExportVo implements Serializable {

	private static final long serialVersionUID = 7920050811030319265L;
	
	public final static String STATUS_EXPORTING = "10";  //正在导出状态
	
	public final static String STATUS_EXPORTED = "20";   //导出完成状态
	
	public final static String STATUS_FAILED = "30";     //导出失败状态
	
	private Long segmentExportId;
	private Long segmentId;
	private String fileType;
	private String columnSetting;
	private Long rowCount;
	private String exportStatus;
	private String exportUserId;
	private java.sql.Timestamp exportTime;
	
	public Long getSegmentExportId() {
		return segmentExportId;
	}
	
	public void setSegmentExportId(Long segmentExportId) {
		this.segmentExportId = segmentExportId;
	}
	
	public Long getSegmentId() {
		return segmentId;
	}
	
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getColumnSetting() {
		return columnSetting;
	}
	
	public void setColumnSetting(String columnSetting) {
		this.columnSetting = columnSetting;
	}
	
	public Long getRowCount() {
		return rowCount;
	}
	
	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}
	
	public String getExportStatus() {
		return exportStatus;
	}
	
	public void setExportStatus(String exportStatus) {
		this.exportStatus = exportStatus;
	}
	
	public String getExportUserId() {
		return exportUserId;
	}
	
	public void setExportUserId(String exportUserId) {
		this.exportUserId = exportUserId;
	}
	
	public java.sql.Timestamp getExportTime() {
		return exportTime;
	}
	
	public void setExportTime(java.sql.Timestamp exportTime) {
		this.exportTime = exportTime;
	}


}
