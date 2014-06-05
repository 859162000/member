package com.wanda.ccs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * EHR员工用户信息
 * 
 * @author Chenxm 2012-09-22
 * 
 */
@Entity
@Table(name = "EHR_WD_USER")
public class EHREmployee extends AbstractEntity implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1528032649759604678L;
	private String employeeCode;//工号
	private String employeeName;//姓名
	private Long id;//员工编号

	private String mobile;

	private String jobName;//岗位名称

	private String employeeStatus;//当前状态（0:未入职  2:正式  3:离职  4:退休  5:内退  6:离休  7:待岗 9:实习  10:实习结束  11:调动中），其中3、4、5、6、10 属于不可用，其余为可用。

	private String sourceType;

	private String employeeType;//雇用类型（详见常数代码R列，但是实际有1、3、4、5、9）

	private Long orgId;//公司编号（某个人所在的公司）（主要关系）
	private String unitName;//部门名称
	private Long jobId;//岗位编号（主要关系）
	private String jobCode;//岗位代码
	private String unitCode;//部门代码
	private String orgCode;//公司代码
	private String orgName;//公司名称
	private Long unitId;//部门编号（某个人所在的部门，也是指向orgID字段）

	private String status;//状态
	private String rtxName;
	
	private String stringDept;
	
	public EHREmployee() {
	}

	public EHREmployee(String employeeCode, String employeeName, Long id,
			String mobile, String jobName, String employeeStatus,
			String sourceType, String employeeType, Long orgId,
			String unitName, Long jobId, String jobCode, String unitCode,
			String orgCode, String orgName, Long unitId, String status,
			String rtxName) {
		super();
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.id = id;
		this.mobile = mobile;
		this.jobName = jobName;
		this.employeeStatus = employeeStatus;
		this.sourceType = sourceType;
		this.employeeType = employeeType;
		this.orgId = orgId;
		this.unitName = unitName;
		this.jobId = jobId;
		this.jobCode = jobCode;
		this.unitCode = unitCode;
		this.orgCode = orgCode;
		this.orgName = orgName;
		this.unitId = unitId;
		this.status = status;
		this.rtxName = rtxName;
	}

	@Column(name = "EMPLOYEECODE", length = 64)
	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Column(name = "EMPLOYEENAME", length = 128)
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	@Id
	@Column(name = "EMPLOYEEID", length = 22,insertable=true,updatable=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MOBILE", length = 128)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "JOBNAME", length = 256)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "EMPLOYEESTATUS", length = 2)
	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	@Column(name = "SOURCETYPE", length = 2)
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "EMPLOYEETYPE", length = 2)
	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	@Column(name = "ORGID", length = 22)
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Column(name = "UNITNAME", length = 256)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "JOBID", length = 22)
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "JOBCODE", length = 256)
	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	@Column(name = "UNITCODE", length = 256)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "ORGCODE", length = 256)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "ORGNAME", length = 256)
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "UNITID", length = 22)
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "USERNAME", length = 1)
	public String getRtxName() {
		return rtxName;
	}

	public void setRtxName(String rtxName) {
		this.rtxName = rtxName;
	}
	
	@Transient
	public String getStringDept() {
		return stringDept;
	}

	public void setStringDept(String stringDept) {
		this.stringDept = stringDept;
	}

}
