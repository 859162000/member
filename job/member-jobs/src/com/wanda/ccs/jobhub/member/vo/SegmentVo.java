package com.wanda.ccs.jobhub.member.vo;


public class SegmentVo extends BaseAuditVo {
	private static final long serialVersionUID = 451896339250572185L;
	
	public final static String STATUS_NONE="10"; //未计算	
	public final static String STATUS_CALCULATING="20";	//计算中
	public final static String STATUS_COMPLETE="30";	//计算完成
	public final static String STATUS_FAILED="40";	//计算失败
	
	public final static String OCCUPIED_NONE = "0";
	public final static String OCCUPIED_BY_CAL_COUNT = "1"; //被客群计算占用
	public final static String OCCUPIED_BY_ACT_RESULT = "2";//被活动波次响应计算占用
	public final static String OCCUPIED_BY_ACT_TARGET = "3";//被活动目标冻结占用
	
	private Long segmentId;
	private String name;
	private String code;
	private String criteriaScheme;
	private String configVersion;
	private String sortName;
	private String sortOrder;
	private Long maxCount = 0L;
	private Long calCount = 0L;// 初始客群数量为0，用于标示计算的状态：  -1为计算中
	private Long controlCount = -1L;
	private Integer controlCountRate = 10;
	private java.sql.Timestamp calCountTime;
	private String allowModifier;
	private Boolean combineSegment;


	//added by fuby 20130728 start
	private String ownerLevel; //创建者所属级别:GROUP:院线,REGION:区域;CINEMA:影城
	private String ownerRegion;//创建者所属区域:区域代码,dim104
	private Long ownerCinema;  //创建者所属影城:影城seqid
	//end of added by fuby
	
	private String status;
	
	public SegmentVo() {}

	public SegmentVo(Long segmentId, String criteriaScheme, String sortName,
			String sortOrder, Long maxCount) {
		super();
		this.segmentId = segmentId;
		this.criteriaScheme = criteriaScheme;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.maxCount = maxCount;
	}

	public String getOwnerLevel() {
		return ownerLevel;
	}

	public void setOwnerLevel(String ownerLevel) {
		this.ownerLevel = ownerLevel;
	}

	public String getOwnerRegion() {
		return ownerRegion;
	}

	public void setOwnerRegion(String ownerRegion) {
		this.ownerRegion = ownerRegion;
	}

	public Long getOwnerCinema() {
		return ownerCinema;
	}

	public void setOwnerCinema(Long ownerCinema) {
		this.ownerCinema = ownerCinema;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
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

	public String getCriteriaScheme() {
		return criteriaScheme;
	}

	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
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

	public String getAllowModifier() {
		return allowModifier;
	}

	public void setAllowModifier(String allowModifier) {
		this.allowModifier = allowModifier;
	}

	public Long retrieveSeqId() {
		return getSegmentId();
	}
	
	public String retrieveCode() {
		return getCode();
	}

	public Boolean getCombineSegment() {
		return combineSegment;
	}

	public void setCombineSegment(Boolean combineSegment) {
		this.combineSegment = combineSegment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getControlCount() {
		return controlCount;
	}

	public void setControlCount(Long controlCount) {
		this.controlCount = controlCount;
	}

	public Integer getControlCountRate() {
		return controlCountRate;
	}

	public void setControlCountRate(Integer controlCountRate) {
		this.controlCountRate = controlCountRate;
	}

}
