package com.wanda.mms.control.stream.vo;
/**
 *  
 * 特殊积分规则表
 * @author wangshuai 
 * @date 2013-05-28
 */
public class T_ext_point_rule {
	/**
	 * 特殊积分规则ID
	 */
	private long ext_point_rule_id;
	/**
	 * 客群ID
	 */
	private long segment_id;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 开始时间(精确到秒)
	 */
	private String start_dtime;
	/**
	 * 结束时间(精确到秒)
	 */
	private String end_dtime;
	/**
	 * 额外积分-百分比
	 */
	private double addition_percent;
	/**
	 * 额外积分-积分值
	 */
	private double addition_code;
	/**
	 * 创建者#CREATE_BY
	 */
	private String create_by;
	/**
	 * 创建时间#CREATE_DATE
	 */
	private String create_date;
	/**
	 * 更新者#UPDATE_BY
	 */
	private String update_by;
	/**
	 * 更新时间#UPDATE_DATE
	 */
	private String update_date;
	/**
	 * 版本号#VERSION
	 */
	private int version;

	public long getExt_point_rule_id() {
		return ext_point_rule_id;
	}

	public void setExt_point_rule_id(long extPointRuleId) {
		ext_point_rule_id = extPointRuleId;
	}

	public long getSegment_id() {
		return segment_id;
	}

	public void setSegment_id(long segmentId) {
		segment_id = segmentId;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart_dtime() {
		return start_dtime;
	}

	public void setStart_dtime(String startDtime) {
		start_dtime = startDtime;
	}

	public String getEnd_dtime() {
		return end_dtime;
	}

	public void setEnd_dtime(String endDtime) {
		end_dtime = endDtime;
	}

	public double getAddition_percent() {
		return addition_percent;
	}

	public void setAddition_percent(double additionPercent) {
		addition_percent = additionPercent;
	}

	public double getAddition_code() {
		return addition_code;
	}

	public void setAddition_code(double additionCode) {
		addition_code = additionCode;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String createBy) {
		create_by = createBy;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String updateBy) {
		update_by = updateBy;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String updateDate) {
		update_date = updateDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
}
