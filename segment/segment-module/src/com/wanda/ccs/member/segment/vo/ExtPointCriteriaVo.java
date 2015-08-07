package com.wanda.ccs.member.segment.vo;

public class ExtPointCriteriaVo extends BaseAuditVo {
	private static final long serialVersionUID = 451896339250572185L;

	private Long extPointCriteriaId;
	private String name; 
	private String code;
	private String criteriaScheme;
	private String configVersion;
	
	private String ownerLevel; //创建者所属级别:GROUP:院线,REGION:区域;CINEMA:影城
	private String ownerRegion;//创建者所属区域:区域代码,dim104
	private Long ownerCinema;  //创建者所属影城:影城seqid
	private String ticketSql;//票房sql
	private String goodsSql;//卖品sql
	private String memberSql;//会员sql

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

	public Long retrieveSeqId() {
		return getExtPointCriteriaId();
	}
	
	public String retrieveCode() {
		return getCode();
	}
	
	public Long getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(Long extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
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

	public String getTicketSql() {
		return ticketSql;
	}

	public void setTicketSql(String ticketSql) {
		this.ticketSql = ticketSql;
	}

	public String getGoodsSql() {
		return goodsSql;
	}

	public void setGoodsSql(String goodsSql) {
		this.goodsSql = goodsSql;
	}

	public String getMemberSql() {
		return memberSql;
	}

	public void setMemberSql(String memberSql) {
		this.memberSql = memberSql;
	}

	
}
