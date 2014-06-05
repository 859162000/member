package com.wanda.ccs.model;

// Generated 2011-10-26 10:19:09 by Hibernate Tools 3.4.0.CR1

import com.xcesys.extras.core.dao.model.VersionableEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * TPpcweekday generated by hbm2java
 */
@Entity
@Table(name = "T_PPCWEEKDAY")
public class TPpcweekday extends VersionableEntity implements
		java.io.Serializable {

	private Long id;
	private Long isdelete;
	private Date updatetime;
	private Long policycellid;

	public TPpcweekday() {
	}

	public TPpcweekday(Long isdelete, Date updatetime, Long policycellid) {
		this.isdelete = isdelete;
		this.updatetime = updatetime;
		this.policycellid = policycellid;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_PPCWEEKDAY")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ISDELETE", precision = 38, scale = 0)
	public Long getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(Long isdelete) {
		this.isdelete = isdelete;
	}

	@Column(name = "UPDATETIME")
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "POLICYCELLID", precision = 38, scale = 0)
	public Long getPolicycellid() {
		return this.policycellid;
	}

	public void setPolicycellid(Long policycellid) {
		this.policycellid = policycellid;
	}

}
