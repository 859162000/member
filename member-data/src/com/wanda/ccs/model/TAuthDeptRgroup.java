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
 * TAuthDeptRgroup generated by hbm2java
 * 赋予组织结构中一个部门的权限组。部门内的用户自动具备赋予部门的权限组中所包含的权限
 */
@Entity
@Table(name = "T_AUTH_DEPT_RGROUP")
public class TAuthDeptRgroup extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 6411418905426612495L;
	
	private Long id; 	//权限组Id
	private TAuthRgroup tAuthRgroup; //部门权限组编号
	private String inheritable; //Y表示可以继承给所有子部门

	public TAuthDeptRgroup() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_AUTH_DEPT_RGROUP")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "AUTH_DEPT_RGROUP_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTH_RGROUP_ID", nullable = false)
	public TAuthRgroup gettAuthRgroup() {
		return this.tAuthRgroup;
	}

	public void settAuthRgroup(TAuthRgroup tAuthRgroup) {
		this.tAuthRgroup = tAuthRgroup;
	}

	@Column(name = "INHERITABLE", nullable = false, length = 1)
	public String getInheritable() {
		return this.inheritable;
	}

	public void setInheritable(String inheritable) {
		this.inheritable = inheritable;
	}

}
