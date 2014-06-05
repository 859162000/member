package com.wanda.ccs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * POS会话
 */
@Entity
@Table(name = "T_INTF_UI_SESSION", uniqueConstraints = @UniqueConstraint(columnNames = "SEQID"))
public class TIntfUiSession extends AbstractEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	/** 员工登录ID */
	private String userId;
	/** 员工姓名 */
	private String userName;
	/** 员工工号 */
	private String userCode;
	private String userAcl;
	private String sessionId;
	private String posType;
	private String opGroup;
	private String opStation;
	private Date beginTime;
	private Date currTime;
	private String cinemaCode;

	public TIntfUiSession() {
	}

	@Id
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CONDUCTOR_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USERACL")
	public String getUserAcl() {
		return userAcl;
	}

	public void setUserAcl(String userAcl) {
		this.userAcl = userAcl;
	}

	@Column(name = "SESSIONID")
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "BEGINTIME")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Column(name = "CINEMACODE")
	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String code) {
		this.cinemaCode = code;
	}

	@Formula("systimestamp")
	public Date getCurrTime() {
		return currTime;
	}

	public void setCurrTime(Date currTime) {
		this.currTime = currTime;
	}

	@Column(name = "CONDUCTOR_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "CONDUCTOR_NUM")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "POS_TYPE")
	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
	}

	@Column(name = "OP_GROUP")
	public String getOpGroup() {
		return opGroup;
	}

	public void setOpGroup(String opGroup) {
		this.opGroup = opGroup;
	}

	@Column(name = "OP_STATION")
	public String getOpStation() {
		return opStation;
	}

	public void setOpStation(String opStation) {
		this.opStation = opStation;
	}
}
