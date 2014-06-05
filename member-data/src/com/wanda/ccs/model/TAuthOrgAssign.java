package com.wanda.ccs.model;

// Generated 2011-11-29 14:55:15 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.SEQUENCE;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * TAuthUser generated by hbm2java
 */
@Entity
@Table(name = "T_AUTH_ORG_ASSIGN")
public class TAuthOrgAssign extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1495439573598628559L;
	
	private Long id;
	private String pkCorp; //TNcBdDeptdoc
	private TCinema tCinema; 
	private String region;//
	private Long cinemaId;
	private String deptPath;//DEPT_PATH
	
	public TAuthOrgAssign() {
	}
	@SequenceGenerator(name = "generator", sequenceName = "S_T_AUTH_ORG_ASSIGN")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "AUTH_ORG_ASSIGN_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "PK_CORP", length = 20)
	public String getPkCorp() {
		return pkCorp;
	}

	public void setPkCorp(String pkCorp) {
		this.pkCorp = pkCorp;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CINEMA", insertable = false, updatable = false)
	public TCinema gettCinema() {
		return this.tCinema;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}
	
	@Column(name = "CINEMA" )
	public Long getCinemaId() {	
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	@Column(name = "REGION", length = 20)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	@Column(name="DEPT_PATH" , length=1024)
	public String getDeptPath() {
		return deptPath;
	}
	public void setDeptPath(String deptPath) {
		this.deptPath = deptPath;
	}

}
