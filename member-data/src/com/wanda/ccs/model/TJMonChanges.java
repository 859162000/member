package com.wanda.ccs.model;

import com.xcesys.extras.core.dao.model.VersionableEntity;

import java.util.Date;
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
@Table(name = "T_JMON_CHANGES")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TJMonChanges extends VersionableEntity implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5732278318289543404L;
	private Long id;//变更编号
	private String fromEName;
	private String fromRTX;
	private String fromPhone;
	private String changeType;//变更类型，C为变更，N为到岗，D为离岗
	private String fromECode;
	private String toECode;
	private String toEName;
	private String toRTX;
	private String toPhone;
	private String approveFlag;//处理状态。W等待处理，Y接受，N不接受
	private String approveBy;//处理人姓名
	private Date approveDate;//处理时间
	private String sourceType;//变更来源类型，E为从EHR监测到的变更，C为总部系统录入的变更
	private String approveById;//处理人账号
	private String requestBy;//如果是从CCS发起，发起人姓名
	private String requestById;//如果是从CCS发起，发起人账号
	private String jobName;
	private String formMainFlag;//Y表示主岗，N表示兼岗
	private String toMainFlag;//Y表示主岗，N表示兼岗
	private EHROrgnization cinemaOrg;
    private EHROrgnization regionOrg;
    private Long cinemaOrgId;//岗位所属影城ORGID
	private Long regionOrgId;//岗位所属区域ORGID。对于直营店，为直营店ORGID，与CINEMA_ORG_ID相同
	public TJMonChanges() {
	}
	 
	 
	public TJMonChanges(Long id, String fromEName,
			String fromRTX, String fromPhone, String changeType,
			String fromECode, String toECode, String toEName, String toRTX,
			String toPhone, String approveFlag, String approveBy,
			Date approveDate, String sourceType, String approveById,
			String requestBy, String requestById, Long cinemaOrgId,
			Long regionOrgId,String jobName, String formMainFlag, String toMainFlag) {
		super();
		this.id = id;
		this.fromEName = fromEName;
		this.fromRTX = fromRTX;
		this.fromPhone = fromPhone;
		this.changeType = changeType;
		this.fromECode = fromECode;
		this.toECode = toECode;
		this.toEName = toEName;
		this.toRTX = toRTX;
		this.toPhone = toPhone;
		this.approveFlag = approveFlag;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.sourceType = sourceType;
		this.approveById = approveById;
		this.requestBy = requestBy;
		this.requestById = requestById;
		this.jobName = jobName;
		this.formMainFlag = formMainFlag;
		this.toMainFlag = toMainFlag;
		this.cinemaOrgId = cinemaOrgId;
		this.regionOrgId = regionOrgId;
	}
	
	   
	@SequenceGenerator(name = "generator", sequenceName = "S_T_JMON_CHANGES" ,allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CHANGE_ID", unique = true, nullable = false, length = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "FROM_E_NAME", length = 128)
	public String getFromEName() {
		return fromEName;
	}
	public void setFromEName(String fromEName) {
		this.fromEName = fromEName;
	}
	@Column(name = "FROM_RTX", length=100)
	public String getFromRTX() {
		return fromRTX;
	}
	public void setFromRTX(String fromRTX) {
		this.fromRTX = fromRTX;
	}
	@Column(name = "FROM_PHONE", length=100)
	public String getFromPhone() {
		return fromPhone;
	}
	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}
	@Column(name = "CHANGE_TYPE", length=1 ,nullable=false)
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	@Column(name = "FROM_E_CODE", length=64)
	public String getFromECode() {
		return fromECode;
	}
	public void setFromECode(String fromECode) {
		this.fromECode = fromECode;
	}
	@Column(name = "TO_E_CODE",length=64)
	public String getToECode() {
		return toECode;
	}
	public void setToECode(String toECode) {
		this.toECode = toECode;
	}
	@Column(name = "TO_E_NAME", length=128)
	public String getToEName() {
		return toEName;
	}
	public void setToEName(String toEName) {
		this.toEName = toEName;
	}
	@Column(name = "TO_RTX", length=100)
	public String getToRTX() {
		return toRTX;
	}
	public void setToRTX(String toRTX) {
		this.toRTX = toRTX;
	}
	  
	@Column(name = "TO_PHONE", length=100)
	public String getToPhone() {
		return toPhone;
	}
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}
	@Column(name = "APPROVE_FLAG", length=1)
	public String getApproveFlag() {
		return approveFlag;
	}
	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}
	@Column(name = "APPROVE_BY",length=40)
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	   
	@Column(name = "APPROVE_DATE")
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	@Column(name = "SOURCE_TYPE", nullable=false)
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	@Column(name = "APPROVE_BY_ID", length=20)
	public String getApproveById() {
		return approveById;
	}
	public void setApproveById(String approveById) {
		this.approveById = approveById;
	}
	   
	@Column(name = "REQUEST_BY", length=40)
	public String getRequestBy() {
		return requestBy;
	}
	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}
	@Column(name = "REQUEST_BY_ID", length=20)
	public String getRequestById() {
		return requestById;
	}
	public void setRequestById(String requestById) {
		this.requestById = requestById;
	}
	@Column(name = "JOB_NAME",length=100,nullable=false)
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	@Column(name = "FROM_MAIN_FLAG",length=1)
	public String getFormMainFlag() {
		return formMainFlag;
	}
	public void setFormMainFlag(String formMainFlag) {
		this.formMainFlag = formMainFlag;
	}
	@Column(name = "TO_MAIN_FLAG", length=1)
	public String getToMainFlag() {
		return toMainFlag;
	}
	public void setToMainFlag(String toMainFlag) {
		this.toMainFlag = toMainFlag;
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

	
}
