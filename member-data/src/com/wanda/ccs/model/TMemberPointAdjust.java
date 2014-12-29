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
 *调整积分
 * @author samchen
 *
 */
@Entity
@Table(name="T_MEMBER_POINT_ADJUST")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberPointAdjust extends BlameableEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -1742900626061727254L;
	
	/**
	 * 主键
	 */
	private Long id;
	private TMember tMember;
	private Long memberId;
	private Date setTime;//生效日期
	private Long levelPoint;//定级积分
	private Long activityPoint;//非定级积分
	private String adjResion;//调整原因
	private String adjReasonType;//调整类型
	private String cinemaInnerCode;//影城编码
	private TCinema tCinema;
	private Long isDeleted;//是否删除
	private String approve;//是否审批

	public TMemberPointAdjust() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_POINT_ADJUST", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_POINT_ADJUST_ID", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Column(name = "SET_TIME", nullable = false)
	public Date getSetTime() {
		return this.setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	@Column(name = "LEVEL_POINT", nullable = false, precision = 22, scale = 0)
	public Long getLevelPoint() {
		return this.levelPoint;
	}

	public void setLevelPoint(Long levelPoint) {
		this.levelPoint = levelPoint;
	}


	@Column(name = "ACTIVITY_POINT", nullable = false, precision = 22, scale = 0)
	public Long getActivityPoint() {
		return this.activityPoint;
	}

	public void setActivityPoint(Long activityPoint) {
		this.activityPoint = activityPoint;
	}

	@Column(name = "ADJ_REASON", length = 4000)
	public String getAdjResion() {
		return this.adjResion;
	}

	public void setAdjResion(String adjResion) {
		this.adjResion = adjResion;
	}


	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "ADJ_REASON_TYPE")
	public String getAdjReasonType() {
		return (adjReasonType+"").trim();
	}

	public void setAdjReasonType(String adjReasonType) {
		this.adjReasonType = (adjReasonType+"").trim();
	}

	@Column(name = "CINEMA_INNER_CODE")
	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}

	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CINEMA_INNER_CODE", insertable = false, updatable = false)
	public TCinema gettCinema() {
		return tCinema;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}

	@Column(name = "ISDELETE")
	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name = "APPROVE")
	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}
	
}
