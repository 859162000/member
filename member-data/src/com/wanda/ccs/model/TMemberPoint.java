package com.wanda.ccs.model;

// Generated 2013-4-26 16:06:19 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * 会员积分
 */
@Entity
@Table(name = "T_MEMBER_POINT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberPoint extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1516518967179893077L;
	private Long id;
	private TMember tMember;
	private Long memberId;
	private Set<TPointHistory> tPointHistory = new HashSet<TPointHistory>(0);
	private Date lastExchangeDate;//最后兑换时间
	private Long activityPoint;// 总累计非定级积分
	private Long levelPointTotal;//总累计定积分
	private Long exgExpirePointBalance;//当年即将过期可兑换积分
	private Long exgPointBalance;//可兑换积分余额
	//最后兑换时间#LAST_EXCHANGE_DATE  总累计非定级积分#ACTIVITY_POINT  总累计定积分#LEVEL_POINT_TOTAL
	//最后发生积分历史ID#POINT_HISTORY_ID  当年即将过期可兑换积分#EXG_EXPIRE_POINT_BALANCE 可兑换积分余额#EXG_POINT_BALANCE
	public TMemberPoint() {
	}


	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_POINT",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_POINT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return this.tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMemberPoints")
	public Set<TPointHistory> gettPointHistory() {
		return tPointHistory;
	}


	public void settPointHistory(Set<TPointHistory> tPointHistory) {
		this.tPointHistory = tPointHistory;
	}

	@Column(name = "LAST_EXCHANGE_DATE")
	public Date getLastExchangeDate() {
		return lastExchangeDate;
	}


	public void setLastExchangeDate(Date lastExchangeDate) {
		this.lastExchangeDate = lastExchangeDate;
	}

	@Column(name = "ACTIVITY_POINT")
	public Long getActivityPoint() {
		return activityPoint;
	}


	public void setActivityPoint(Long activityPoint) {
		this.activityPoint = activityPoint;
	}

	@Column(name = "LEVEL_POINT_TOTAL")
	public Long getLevelPointTotal() {
		return levelPointTotal;
	}


	public void setLevelPointTotal(Long levelPointTotal) {
		this.levelPointTotal = levelPointTotal;
	}

	@Column(name = "EXG_EXPIRE_POINT_BALANCE")
	public Long getExgExpirePointBalance() {
		return exgExpirePointBalance;
	}


	public void setExgExpirePointBalance(Long exgExpirePointBalance) {
		this.exgExpirePointBalance = exgExpirePointBalance;
	}

	@Column(name = "EXG_POINT_BALANCE")
	public Long getExgPointBalance() {
		return exgPointBalance;
	}


	public void setExgPointBalance(Long exgPointBalance) {
		this.exgPointBalance = exgPointBalance;
	}


}
