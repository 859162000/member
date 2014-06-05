package com.wanda.ccs.model;

// Generated 2013-5-21 10:54:02 by Hibernate Tools 3.3.0.GA
import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

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
 * TMemberLevel generated by hbm2java
 */
@Entity
@Table(name = "T_MEMBER_LEVEL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberLevel extends VersionableEntity implements
		java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6702274319182934878L;
	private Long id;
	private String memLevel;
	private Date expireDate;
	private String orgLevel;
	private Date setTime;
	private String targetLevel;
	private Long levelPointOffset;
	private Long ticketOffset;
	private String changeLeveNo;
	private Long memberId;
	private TMember tMember;
//	private Set<TLevelHistory> tLevelHistory = new HashSet<TLevelHistory>();

	public TMemberLevel() {
	}

	@Column(name = "MEMBER_ID", nullable = false, precision = 22, scale = 0)
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MEM_LEVEL", nullable = false, length = 20)
	public String getMemLevel() {
		return this.memLevel;
	}

	public void setMemLevel(String memLevel) {
		this.memLevel = memLevel;
	}

	@Column(name = "EXPIRE_DATE")
	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "ORG_LEVEL", nullable = false, length = 20)
	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	@Column(name = "SET_TIME")
	public Date getSetTime() {
		return this.setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	@Column(name = "TARGET_LEVEL", nullable = false, length = 20)
	public String getTargetLevel() {
		return this.targetLevel;
	}

	public void setTargetLevel(String targetLevel) {
		this.targetLevel = targetLevel;
	}

	@Column(name = "LEVEL_POINT_OFFSET", nullable = false, precision = 22, scale = 0)
	public Long getLevelPointOffset() {
		return this.levelPointOffset;
	}

	public void setLevelPointOffset(Long levelPointOffset) {
		this.levelPointOffset = levelPointOffset;
	}

	@Column(name = "TICKET_OFFSET", nullable = false, precision = 22, scale = 0)
	public Long getTicketOffset() {
		return this.ticketOffset;
	}

	public void setTicketOffset(Long ticketOffset) {
		this.ticketOffset = ticketOffset;
	}

	@Column(name = "CHANGE_LEVEL_NO", length = 128)
	public String getChangeLeveNo() {
		return this.changeLeveNo;
	}

	public void setChangeLeveNo(String changeLeveNo) {
		this.changeLeveNo = changeLeveNo;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_LEVEL", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "LEVEL_ID", nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMemberLevel")
//	public Set<TLevelHistory> gettLevelHistory() {
//		return tLevelHistory;
//	}
//
//	public void settLevelHistory(Set<TLevelHistory> tLevelHistory) {
//		this.tLevelHistory = tLevelHistory;
//	}

}
