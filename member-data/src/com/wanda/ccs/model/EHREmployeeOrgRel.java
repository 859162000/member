package com.wanda.ccs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.AbstractEntity;
/**
 * EHR员工组织中间表
 * @author Chenxm 
 * 2012-09-22
 *
 */
@Entity
@Table(name = "EHR_WD_USER_ORG_REL")
public class EHREmployeeOrgRel extends AbstractEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4202556026781120007L;
	private Long id;
	private Long unitId;
	private String mainOrg;
	private String employeeCode;
	private String unitCode;
	public EHREmployeeOrgRel() {
	}

	public EHREmployeeOrgRel(Long id, Long unitId, String mainOrg,String employeeCode ,String unitCode) {
		super();
		this.id = id;
		this.unitId = unitId;
		this.mainOrg = mainOrg;
		this.employeeCode = employeeCode;
		this.unitCode = unitCode;
	}
	@Id
	@Column(name = "EMPLOYEEID",  length = 22 ,insertable=true,updatable=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "UNITID", length = 22)
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "ISMAINORG", length = 64)
	public String getMainOrg() {
		return mainOrg;
	}

	public void setMainOrg(String mainOrg) {
		this.mainOrg = mainOrg;
	}
	@Column(name = "EMPLOYEECODE", length = 64)
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	@Column(name = "UNITCODE", length = 256)
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

}
