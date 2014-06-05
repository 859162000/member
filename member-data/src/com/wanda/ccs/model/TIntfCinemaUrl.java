package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * TIntfCinemaUrl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_INTF_CINEMA_URL")
public class TIntfCinemaUrl extends BlameableEntity implements java.io.Serializable {
	private static final long serialVersionUID = 9218694549402078649L;
	
	private Long seqid;
	private String lockurl;
	private String unlockurl;
	private Long cinemaid;
	private String ftphost;
	private String ftpport;
	private String ftpuser;
	private String ftppwd;

	// Constructors

	/** default constructor */
	public TIntfCinemaUrl() {
	}

	/** full constructor */
	public TIntfCinemaUrl(Long seqid, String lockurl, String unlockurl,
			Long cinemaid, String ftphost, String ftpport,
			String ftpuser, String ftppwd) {
		this.seqid = seqid;
		this.lockurl = lockurl;
		this.unlockurl = unlockurl;
		this.cinemaid = cinemaid;
		this.ftphost = ftphost;
		this.ftpport = ftpport;
		this.ftpuser = ftpuser;
		this.ftppwd = ftppwd;
	}

	// Property accessors
	@SequenceGenerator(name = "generator", sequenceName = "S_T_INTF_CINEMA_URL", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getSeqid() {
		return this.seqid;
	}

	public void setSeqid(Long seqid) {
		this.seqid = seqid;
	}

	@Column(name = "LOCKURL", length = 200)
	public String getLockurl() {
		return this.lockurl;
	}

	public void setLockurl(String lockurl) {
		this.lockurl = lockurl;
	}

	@Column(name = "UNLOCKURL", length = 200)
	public String getUnlockurl() {
		return this.unlockurl;
	}

	public void setUnlockurl(String unlockurl) {
		this.unlockurl = unlockurl;
	}

	@Column(name = "CINEMAID", precision = 22, scale = 0)
	public Long getCinemaid() {
		return this.cinemaid;
	}

	public void setCinemaid(Long cinemaid) {
		this.cinemaid = cinemaid;
	}

	@Column(name = "FTPHOST", length = 20)
	public String getFtphost() {
		return this.ftphost;
	}

	public void setFtphost(String ftphost) {
		this.ftphost = ftphost;
	}

	@Column(name = "FTPPORT", length = 20)
	public String getFtpport() {
		return this.ftpport;
	}

	public void setFtpport(String ftpport) {
		this.ftpport = ftpport;
	}

	@Column(name = "FTPUSER", length = 20)
	public String getFtpuser() {
		return this.ftpuser;
	}

	public void setFtpuser(String ftpuser) {
		this.ftpuser = ftpuser;
	}

	@Column(name = "FTPPWD", length = 20)
	public String getFtppwd() {
		return this.ftppwd;
	}

	public void setFtppwd(String ftppwd) {
		this.ftppwd = ftppwd;
	}
	
	@Transient
	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long arg0) {
	}

}