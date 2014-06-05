package com.wanda.ccs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.AbstractEntity;
/**
 * EHR组织机构
 * @author Chenxm 
 * 2012-09-22
 *
 */
@Entity
@Table(name = "EHR_WD_ORG")
public class EHROrgnization extends AbstractEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 77665692228164435L;
	
	private Long id;
	
	private String status;
	private Long parentUnitId;
	private String shortName;
	private String changeStatus;
	private Date changeDate;
	private String orgCode;
	private String orgName;
	private Long orgType;
	private String parentUnitCode;
	private String parentStauts;
	
	public EHROrgnization() {
	}

	
	public EHROrgnization(Long id, String status, Long parentUnitId,
			String shortName, String changeStatus, Date changeDate,
			String orgCode, String orgName, Long orgType,
			String parentUnitCode, String parentStauts) {
		super();
		this.id = id;
		this.status = status;
		this.parentUnitId = parentUnitId;
		this.shortName = shortName;
		this.changeStatus = changeStatus;
		this.changeDate = changeDate;
		this.orgCode = orgCode;
		this.orgName = orgName;
		this.orgType = orgType;
		this.parentUnitCode = parentUnitCode;
		this.parentStauts = parentStauts;
	}
	@Column(name = "ORGNAME", length = 2048)
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	@Column(name = "ORGTYPE", length = 22)
	public Long getOrgType() {
		return orgType;
	}
	public void setOrgType(Long orgType) {
		this.orgType = orgType;
	}
	@Id
	@Column(name = "ORGID",  length = 22 ,insertable=true,updatable=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "PARENTUNITID", length = 22)
	public Long getParentUnitId() {
		return parentUnitId;
	}

	public void setParentUnitId(Long parentUnitId) {
		this.parentUnitId = parentUnitId;
	}
	@Column(name = "STATUS", length = 64)
	public String getStatus() {
		return status;
	}
	@Column(name = "SHORTNAME", length = 1024)
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Column(name = "CHANGESTATUS", length = 64)
	public String getChangeStatus() {
		return changeStatus;
	}
	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}
	@Column(name = "CHANGEDATE")
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	@Column(name = "ORGCODE", length = 64)
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Column(name = "PARENTUNITCODE", length = 1024)
	public String getParentUnitCode() {
		return parentUnitCode;
	}
	public void setParentUnitCode(String parentUnitCode) {
		this.parentUnitCode = parentUnitCode;
	}
	@Column(name = "PARENTSTATUS", length = 64)
	public String getParentStauts() {
		return parentStauts;
	}
	public void setParentStauts(String parentStauts) {
		this.parentStauts = parentStauts;
	}
	public void setStatus(String status) {
		this.status = status;
	}




}
