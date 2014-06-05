package com.wanda.mms.control.stream.vo;
/**
 *  
 * 客户群表
 * @author wangshuai 
 * @date 2013-05-29
 */
public class T_segment {
	/**
	 * 客群ID	
	 */
	private long segment_id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 客群类型
	 */
	private String type;
	/**
	 * 查询条件配置
	 */
	private String query_conditions;
	/**
	 * 配置版本号
	 */
	private String config_version;
	/**
	 * 排序条件名称
	 */
	private String sort_name;
	/**
	 * 排序顺序
	 */
	private String sort_order;
	/**
	 * 数量
	 */
	private long count;
	/**
	 * 数量计算时间
	 */
	private String count_time;
	/**
	 * 创建方级别
	 */
	private String creation_level;
	/**
	 * 创建方-影城ID
	 */
	private long creation_cinema_id;
	/**
	 * 创建方-区域ID
	 */
	private String creation_area_id;
	/**
	 * 授权修改人
	 */
	private String allow_modifier;
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
	public long getSegment_id() {
		return segment_id;
	}
	public void setSegment_id(long segmentId) {
		segment_id = segmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQuery_conditions() {
		return query_conditions;
	}
	public void setQuery_conditions(String queryConditions) {
		query_conditions = queryConditions;
	}
	public String getConfig_version() {
		return config_version;
	}
	public void setConfig_version(String configVersion) {
		config_version = configVersion;
	}
	public String getSort_name() {
		return sort_name;
	}
	public void setSort_name(String sortName) {
		sort_name = sortName;
	}
	public String getSort_order() {
		return sort_order;
	}
	public void setSort_order(String sortOrder) {
		sort_order = sortOrder;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getCount_time() {
		return count_time;
	}
	public void setCount_time(String countTime) {
		count_time = countTime;
	}
	public String getCreation_level() {
		return creation_level;
	}
	public void setCreation_level(String creationLevel) {
		creation_level = creationLevel;
	}
	public long getCreation_cinema_id() {
		return creation_cinema_id;
	}
	public void setCreation_cinema_id(long creationCinemaId) {
		creation_cinema_id = creationCinemaId;
	}
	public String getCreation_area_id() {
		return creation_area_id;
	}
	public void setCreation_area_id(String creationAreaId) {
		creation_area_id = creationAreaId;
	}
	public String getAllow_modifier() {
		return allow_modifier;
	}
	public void setAllow_modifier(String allowModifier) {
		allow_modifier = allowModifier;
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
