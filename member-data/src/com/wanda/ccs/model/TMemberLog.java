package com.wanda.ccs.model;

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

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 会员日志表
 * 
 * @author chenxm
 * 
 */
@Entity
@Table(name = "T_MEMBER_LOG")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberLog extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = 167531542032863488L;

	private Long id;
	private Long meberId;
	private TMember tMember;
	private String changedBy;
	private Date changedDate;
	private String memberStatus;
	private String memberDeleted;
	private String memberMobile;
	private Long registCinemaId;
	private TCinema tCinema;
	private String isContactable;
	private Date birthday;// 会员生日

	public TMemberLog() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_LOG", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_LOG_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MEMBER_ID")
	public Long getMeberId() {
		return meberId;
	}

	public void setMeberId(Long meberId) {
		this.meberId = meberId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

	@Column(name = "CHANGEDBY")
	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@Column(name = "CHANGEDDATE")
	public Date getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

	@Column(name = "MEMBER_DELETED")
	public String getMemberDeleted() {
		return memberDeleted;
	}

	public void setMemberDeleted(String memberDeleted) {
		this.memberDeleted = memberDeleted;
	}

	@Column(name = "MEMBER_MOBILE")
	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	@Column(name = "REGIST_CINEMA_ID")
	public Long getRegistCinemaId() {
		return registCinemaId;
	}

	public void setRegistCinemaId(Long registCinemaId) {
		this.registCinemaId = registCinemaId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGIST_CINEMA_ID", insertable = false, updatable = false)
	public TCinema gettCinema() {
		return tCinema;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}
	
	@Column(name = "ISCONTACTABLE")
	public String getIsContactable() {
		return isContactable;
	}

	public void setIsContactable(String isContactable) {
		this.isContactable = isContactable;
	}

	@Column(name = "MEMBER_STATUS")
	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	
}
