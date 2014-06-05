package com.wanda.ccs.model;

// Generated 2011-11-29 14:55:15 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.wanda.ccs.auth.CcsUserProfile;
import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * 与报表系统集成的会话
 */
@Entity
@Table(name = "T_AUTH_RPT_SESSION", uniqueConstraints = @UniqueConstraint(columnNames = "SESSION_KEY"))
public class TAuthRptSession extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1495439573598628559L;

	private Long id;
	private String systemId;
	private String sessionKey;
	private Long cinemaId; // 所属影城id
	private String loginId; // 用户登录账号
	private String userLevel;// 用户所属级别
	private String userName; // 用户姓名
	private String userEmail;// 用户email
	private String region;// 用户所属区域
	private String unitCode;
	private String unitName;
	private String rights;
	private Date currTime;

	public TAuthRptSession() {

	}

	public TAuthRptSession(CcsUserProfile user, String system, String sessionKey) {
		setSystemId(system);
		setSessionKey(sessionKey);
		setCinemaId(user.getCinemaId());
		setLoginId(user.getId());
		setUserName(user.getName());
		setUserEmail(user.getEmail());
		setUserLevel(user.getLevel().name().substring(0, 1));
		setRegion(user.getRegionCode());
		setUnitCode(user.getNcUnitCode());
		setUnitName(user.getNcUnitName());
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_AUTH_RPT_SESSION")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "AUTH_RPT_SESSION_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SESSION_KEY", nullable = false, length = 50)
	public String getSessionKey() {
		return this.sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@Column(name = "CINEMA")
	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	@Column(name = "LOGIN_ID", unique = true, length = 20)
	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "USER_LEVEL", length = 1)
	public String getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	@Column(name = "USER_NAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_EMAIL", length = 100)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "REGION", length = 20)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "SYSTEM_ID", length = 50)
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "UNIT_CODE", length = 40)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "UNIT_NAME", length = 200)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "RIGHTS")
	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	@Formula("systimestamp")
	public Date getCurrTime() {
		return currTime;
	}

	public void setCurrTime(Date currTime) {
		this.currTime = currTime;
	}
}
