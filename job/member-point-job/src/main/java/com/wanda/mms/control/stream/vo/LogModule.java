package com.wanda.mms.control.stream.vo;



/**
 *  
 * 日志表
 * @author wangshuai 
 * @date 2013-04-15
 */
public class LogModule {
	/**
	 * 日志ID
	 */
	private Long logModuleId;
	/**
	 * 数据日期 YYYYMMDD 8位数
	 */
	private Long dataYMD;
	/**
	 * 模块ID. 1-数据采集 2-指标计算 3-发送任务生成 4-发送器
	 */
	private int moduleID;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime; 
	/**
	 * 执行结果 0-成功，其他-失败
	 */
	private int status;
	/**
	 * 结果描述 DESCRIPTION
	 */
	private String description;
	
	public Long getLogModuleId() {
		return logModuleId;
	}
	public void setLogModuleId(Long logModuleId) {
		this.logModuleId = logModuleId;
	}
	public Long getDataYMD() {
		return dataYMD;
	}
	public void setDataYMD(Long dataYMD) {
		this.dataYMD = dataYMD;
	}

	public int getModuleID() {
		return moduleID;
	}
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	
	

}
