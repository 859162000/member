package com.wanda.ccs.model;

import com.xcesys.extras.core.dao.model.VersionableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Chenxm 2012-09-22
 * 
 */
@Entity
@Table(name = "T_JMON_OFFICIAL_JOBS")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TJMonOfficialJobs extends VersionableEntity implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8502501939859648731L;
	private Long id;//officialjobid关联关系编号
	private String ehrECode;//EHR中的员工编码
	private String empName;//员工姓名
	private String rtxName;
	private String phone;
	private Long cinemaOrgId;//岗位所属影城ORGID
	private Long regionOrgId;//岗位所属区域ORGID。对于直营店，为直营店ORGID，与CINEMA_ORG_ID相同
	private String jobName;//岗位名称
	private String mainFlag;//Y表示主岗，N表示兼岗
	private String code;//员工编码，如果是CCS用户录入则为空。
    private EHROrgnization cinemaOrg;
    private EHROrgnization regionOrg;
	public TJMonOfficialJobs() {
	}

	public TJMonOfficialJobs(Long id, String ehrECode, String empName,
			String rtxName, String phone,  Long cinemaOrgId,
			Long regionOrgId,String jobName, String mainFlag, String code) {
		super();
		this.id = id;
		this.ehrECode = ehrECode;
		this.empName = empName;
		this.rtxName = rtxName;
		this.phone = phone;
		this.cinemaOrgId = cinemaOrgId;
		this.regionOrgId = regionOrgId;
		this.jobName = jobName;
		this.mainFlag = mainFlag;
		this.code = code;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_JMON_OFFICIAL_JOBS", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "OFFICIAL_JOB_ID", unique = true, nullable = false, length = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CINEMA_ORG_ID", length = 100, nullable = false)
	public Long getCinemaOrgId() {
		return cinemaOrgId;
	}

	public void setCinemaOrgId(Long cinemaOrgId) {
		this.cinemaOrgId = cinemaOrgId;
	}
	@Column(name = "REGION_ORG_ID", length = 100, nullable = false)
	public Long getRegionOrgId() {
		return regionOrgId;
	}

	public void setRegionOrgId(Long regionOrgId) {
		this.regionOrgId = regionOrgId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CINEMA_ORG_ID", insertable = false, updatable = false)
	public EHROrgnization getCinemaOrg() {
		return cinemaOrg;
	}
	
	public void setCinemaOrg(EHROrgnization cinemaOrg) {
		this.cinemaOrg = cinemaOrg;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGION_ORG_ID", insertable = false, updatable = false)
	public EHROrgnization getRegionOrg() {
		return regionOrg;
	}

	public void setRegionOrg(EHROrgnization regionOrg) {
		this.regionOrg = regionOrg;
	}

	@Column(name = "JOB_NAME", length = 100, nullable = false)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "EHR_E_CODE", length = 64)
	public String getEhrECode() {
		return ehrECode;
	}

	public void setEhrECode(String ehrECode) {
		this.ehrECode = ehrECode;
	}

	@Column(name = "E_NAME", length = 128)
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	@Column(name = "RTX", length = 100)
	public String getRtxName() {
		return rtxName;
	}

	public void setRtxName(String rtxName) {
		this.rtxName = rtxName;
	}

	@Column(name = "PHONE", length = 100)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "MAIN_FLAG", length = 1, nullable = false)
	public String getMainFlag() {
		if(mainFlag == null ){
			mainFlag = "Y";
		}
		return mainFlag;
	}

	public void setMainFlag(String mainFlag) {
		
		this.mainFlag = mainFlag;
	}

	@Column(name = "E_CODE", length = 64)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
