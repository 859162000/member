package com.wanda.ccs.model;

// Generated 2012-1-12 17:18:19 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * TNcBdPsndoc generated by hbm2java
 */
@Entity
@Table(name = "T_NC_BD_PSNDOC")
public class TNcBdPsndoc extends AbstractEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = -2226277936727812678L;
	
	private String pkPsndoc;
	private Integer dr;
	private String ts;
	private String pkPsnbasdoc;
	private String psncode;
	private String psnname;
	private String pkPsncl;
	private String pkDeptdoc;
	private String pkOmJob;
	private String amcode;
	private String pkCorp;
	private String sealdate;
	private String maxinnercode;
	private String innercode;
	private Integer psnclscope;
	private String jobrank;
	private String jobseries;
	private String clerkflag;
	private String clerkcode;
	private String indutydate;
	private String outdutydate;
	private String indocflag;
	private String pkDutyrank;
	private Integer showorder;
	private String pkClerkclass;
	private String pkPsntype;
	private String directleader;

	public TNcBdPsndoc() {
	}

	public TNcBdPsndoc(Integer dr, String ts, String pkPsnbasdoc,
			String psncode, String psnname, String pkPsncl, String pkDeptdoc,
			String pkOmJob, String amcode, String pkCorp, String sealdate,
			String maxinnercode, String innercode, Integer psnclscope,
			String jobrank, String jobseries, String clerkflag,
			String clerkcode, String indutydate, String outdutydate,
			String indocflag, String pkDutyrank, Integer showorder,
			String pkClerkclass, String pkPsntype, String directleader) {
		this.dr = dr;
		this.ts = ts;
		this.pkPsnbasdoc = pkPsnbasdoc;
		this.psncode = psncode;
		this.psnname = psnname;
		this.pkPsncl = pkPsncl;
		this.pkDeptdoc = pkDeptdoc;
		this.pkOmJob = pkOmJob;
		this.amcode = amcode;
		this.pkCorp = pkCorp;
		this.sealdate = sealdate;
		this.maxinnercode = maxinnercode;
		this.innercode = innercode;
		this.psnclscope = psnclscope;
		this.jobrank = jobrank;
		this.jobseries = jobseries;
		this.clerkflag = clerkflag;
		this.clerkcode = clerkcode;
		this.indutydate = indutydate;
		this.outdutydate = outdutydate;
		this.indocflag = indocflag;
		this.pkDutyrank = pkDutyrank;
		this.showorder = showorder;
		this.pkClerkclass = pkClerkclass;
		this.pkPsntype = pkPsntype;
		this.directleader = directleader;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_NC_BD_PSNDOC")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "PK_PSNDOC", unique = true, nullable = false, length = 20)
	public String getPkPsndoc() {
		return this.pkPsndoc;
	}

	public void setPkPsndoc(String pkPsndoc) {
		this.pkPsndoc = pkPsndoc;
	}

	@Column(name = "DR", precision = 22, scale = 0)
	public Integer getDr() {
		return this.dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	@Column(name = "TS", length = 19)
	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	@Column(name = "PK_PSNBASDOC", length = 20)
	public String getPkPsnbasdoc() {
		return this.pkPsnbasdoc;
	}

	public void setPkPsnbasdoc(String pkPsnbasdoc) {
		this.pkPsnbasdoc = pkPsnbasdoc;
	}

	@Column(name = "PSNCODE", length = 40)
	public String getPsncode() {
		return this.psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	@Column(name = "PSNNAME", length = 200)
	public String getPsnname() {
		return this.psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	@Column(name = "PK_PSNCL", length = 20)
	public String getPkPsncl() {
		return this.pkPsncl;
	}

	public void setPkPsncl(String pkPsncl) {
		this.pkPsncl = pkPsncl;
	}

	@Column(name = "PK_DEPTDOC", length = 20)
	public String getPkDeptdoc() {
		return this.pkDeptdoc;
	}

	public void setPkDeptdoc(String pkDeptdoc) {
		this.pkDeptdoc = pkDeptdoc;
	}

	@Column(name = "PK_OM_JOB", length = 20)
	public String getPkOmJob() {
		return this.pkOmJob;
	}

	public void setPkOmJob(String pkOmJob) {
		this.pkOmJob = pkOmJob;
	}

	@Column(name = "AMCODE", length = 50)
	public String getAmcode() {
		return this.amcode;
	}

	public void setAmcode(String amcode) {
		this.amcode = amcode;
	}

	@Column(name = "PK_CORP", length = 4)
	public String getPkCorp() {
		return this.pkCorp;
	}

	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}

	@Column(name = "SEALDATE", length = 10)
	public String getSealdate() {
		return this.sealdate;
	}

	public void setSealdate(String sealdate) {
		this.sealdate = sealdate;
	}

	@Column(name = "MAXINNERCODE", length = 60)
	public String getMaxinnercode() {
		return this.maxinnercode;
	}

	public void setMaxinnercode(String maxinnercode) {
		this.maxinnercode = maxinnercode;
	}

	@Column(name = "INNERCODE", length = 60)
	public String getInnercode() {
		return this.innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	@Column(name = "PSNCLSCOPE", precision = 22, scale = 0)
	public Integer getPsnclscope() {
		return this.psnclscope;
	}

	public void setPsnclscope(Integer psnclscope) {
		this.psnclscope = psnclscope;
	}

	@Column(name = "JOBRANK", length = 20)
	public String getJobrank() {
		return this.jobrank;
	}

	public void setJobrank(String jobrank) {
		this.jobrank = jobrank;
	}

	@Column(name = "JOBSERIES", length = 20)
	public String getJobseries() {
		return this.jobseries;
	}

	public void setJobseries(String jobseries) {
		this.jobseries = jobseries;
	}

	@Column(name = "CLERKFLAG", length = 1)
	public String getClerkflag() {
		return this.clerkflag;
	}

	public void setClerkflag(String clerkflag) {
		this.clerkflag = clerkflag;
	}

	@Column(name = "CLERKCODE", length = 25)
	public String getClerkcode() {
		return this.clerkcode;
	}

	public void setClerkcode(String clerkcode) {
		this.clerkcode = clerkcode;
	}

	@Column(name = "INDUTYDATE", length = 10)
	public String getIndutydate() {
		return this.indutydate;
	}

	public void setIndutydate(String indutydate) {
		this.indutydate = indutydate;
	}

	@Column(name = "OUTDUTYDATE", length = 10)
	public String getOutdutydate() {
		return this.outdutydate;
	}

	public void setOutdutydate(String outdutydate) {
		this.outdutydate = outdutydate;
	}

	@Column(name = "INDOCFLAG", length = 1)
	public String getIndocflag() {
		return this.indocflag;
	}

	public void setIndocflag(String indocflag) {
		this.indocflag = indocflag;
	}

	@Column(name = "PK_DUTYRANK", length = 20)
	public String getPkDutyrank() {
		return this.pkDutyrank;
	}

	public void setPkDutyrank(String pkDutyrank) {
		this.pkDutyrank = pkDutyrank;
	}

	@Column(name = "SHOWORDER", precision = 22, scale = 0)
	public Integer getShoworder() {
		return this.showorder;
	}

	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}

	@Column(name = "PK_CLERKCLASS", length = 20)
	public String getPkClerkclass() {
		return this.pkClerkclass;
	}

	public void setPkClerkclass(String pkClerkclass) {
		this.pkClerkclass = pkClerkclass;
	}

	@Column(name = "PK_PSNTYPE", length = 20)
	public String getPkPsntype() {
		return this.pkPsntype;
	}

	public void setPkPsntype(String pkPsntype) {
		this.pkPsntype = pkPsntype;
	}

	@Column(name = "DIRECTLEADER", length = 20)
	public String getDirectleader() {
		return this.directleader;
	}

	public void setDirectleader(String directleader) {
		this.directleader = directleader;
	}

	@Override
	@Transient
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long arg0) {
		
	}

}
