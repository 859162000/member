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
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * TAuthUserRight generated by hbm2java
 * 用户被赋予的权限
 */
@Entity
@Table(name = "T_AUTH_USER_RIGHT")
public class TAuthUserRight extends BlameableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = -2701155643354977699L;
	
	private Long id;
	private TAuthRight tAuthRight;//权限Id
	private TAuthUser tAuthUser;//用户Id
	private boolean delete = false;
	private Long tAuthRightId;
	private String centerLevel;
	public TAuthUserRight() {
	}
	
	public TAuthUserRight(Long tAuthRightId){
		this.tAuthRightId = tAuthRightId;
	}
	public TAuthUserRight(TAuthRight tAuthRight, TAuthUser tAuthUser) {
		this.tAuthRight = tAuthRight;
		this.tAuthUser = tAuthUser;
	}
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_AUTH_USER_RIGHT")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "AUTH_USER_RIGHT_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTH_RIGHT_ID", nullable = false, insertable = false, updatable = false)
	public TAuthRight gettAuthRight() {
		return this.tAuthRight;
	}

	public void settAuthRight(TAuthRight tAuthRight) {
		this.tAuthRight = tAuthRight;
	}
	
	@Column(name="AUTH_RIGHT_ID")
	public Long gettAuthRightId() {
		return tAuthRightId;
	}

	public void settAuthRightId(Long tAuthRightId) {
		this.tAuthRightId = tAuthRightId;
	}
	
	@Column(name="CENTER_LEVEL",nullable=true)
	public String getCenterLevel() {
		return centerLevel;
	}

	public void setCenterLevel(String centerLevel) {
		this.centerLevel = centerLevel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTH_USER_ID", nullable = false)
	public TAuthUser gettAuthUser() {
		return this.tAuthUser;
	}

	public void settAuthUser(TAuthUser tAuthUser) {
		this.tAuthUser = tAuthUser;
	}
	
	@Transient
	public boolean getDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
