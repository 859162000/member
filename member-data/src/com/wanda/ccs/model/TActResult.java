package com.wanda.ccs.model;

// Generated 2013-5-26 15:29:38 by Hibernate Tools 3.4.0.CR1

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

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * TActResult generated by hbm2java
 * 活动波次执行结果
 */
@Entity
@Table(name = "T_ACT_RESULT")
public class TActResult extends BlameableEntity implements java.io.Serializable {
	private static final long serialVersionUID = 2997873864568745075L;
	
	private Long id;
	private TCmnActivity tCmnActivity;//波次
	private Long cmnActivityId;//波次ID
	private TVoucherPool tVoucherPool;//券库
	private TExtPointRule tExtPointRule;//特殊积分规则
	private Long contactCount;//联络人数（发送人数）
	private Long controlCount;//控制组数量
	private String resConfigType;//响应统计方式-DIM1010
	private Long resSegmentId;//推荐响应客群ID
	private Long alterSegmentId;//关联响应客群ID
	private TSegment resSegment;
	private TSegment alterSegment;
	private Long controlResCount;//控制组响应人数
	private Long resCount;//推荐响应人数
	private Long alterResCount;//关联响应人数
	
	private String status;//活动波次执行结果状态  10执行中  20执行结束


	public TActResult() {
	}


	@SequenceGenerator(name = "generator", sequenceName = "S_T_ACT_RESULT", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ACT_RESULT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CMN_ACTIVITY_ID", insertable = false, updatable = false)
	public TCmnActivity gettCmnActivity() {
		return this.tCmnActivity;
	}

	public void settCmnActivity(TCmnActivity tCmnActivity) {
		this.tCmnActivity = tCmnActivity;
	}
	
	@Column(name = "CMN_ACTIVITY_ID")
	public Long getCmnActivityId() {
		return cmnActivityId;
	}

	public void setCmnActivityId(Long cmnActivityId) {
		this.cmnActivityId = cmnActivityId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOUCHER_POOL_ID")
	public TVoucherPool gettVoucherPool() {
		return this.tVoucherPool;
	}

	public void settVoucherPool(TVoucherPool tVoucherPool) {
		this.tVoucherPool = tVoucherPool;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXT_POINT_RULE_ID")
	public TExtPointRule gettExtPointRule() {
		return this.tExtPointRule;
	}

	public void settExtPointRule(TExtPointRule tExtPointRule) {
		this.tExtPointRule = tExtPointRule;
	}

	@Column(name = "CONTACT_COUNT", precision = 22, scale = 0)
	public Long getContactCount() {
		return this.contactCount;
	}

	public void setContactCount(Long contactCount) {
		this.contactCount = contactCount;
	}

	@Column(name = "RES_CONFIG_TYPE", nullable = false, length = 20)
	public String getResConfigType() {
		return this.resConfigType;
	}

	public void setResConfigType(String resConfigType) {
		this.resConfigType = resConfigType;
	}

	@Column(name = "RES_SEGMENT_ID", precision = 22, scale = 0)
	public Long getResSegmentId() {
		return this.resSegmentId;
	}

	public void setResSegmentId(Long resSegmentId) {
		this.resSegmentId = resSegmentId;
	}

	@Column(name = "ALTER_SEGMENT_ID", precision = 22, scale = 0)
	public Long getAlterSegmentId() {
		return this.alterSegmentId;
	}

	public void setAlterSegmentId(Long alterSegmentId) {
		this.alterSegmentId = alterSegmentId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RES_SEGMENT_ID", insertable = false, updatable = false)
	public TSegment getResSegment() {
		return resSegment;
	}

	public void setResSegment(TSegment resSegment) {
		this.resSegment = resSegment;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALTER_SEGMENT_ID", insertable = false, updatable = false)
	public TSegment getAlterSegment() {
		return alterSegment;
	}

	public void setAlterSegment(TSegment alterSegment) {
		this.alterSegment = alterSegment;
	}
	@Column(name = "CONTROL_RES_COUNT", precision = 22, scale = 0)
	public Long getControlResCount() {
		return this.controlResCount;
	}

	public void setControlResCount(Long controlResCount) {
		this.controlResCount = controlResCount;
	}

	@Column(name = "RES_COUNT", precision = 22, scale = 0)
	public Long getResCount() {
		return this.resCount;
	}

	public void setResCount(Long resCount) {
		this.resCount = resCount;
	}

	@Column(name = "ALTER_RES_COUNT", precision = 22, scale = 0)
	public Long getAlterResCount() {
		return this.alterResCount;
	}

	public void setAlterResCount(Long alterResCount) {
		this.alterResCount = alterResCount;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "CONTROL_COUNT", precision = 22, scale = 0)
	public Long getControlCount() {
		return this.controlCount;
	}

	public void setControlCount(Long controlCount) {
		this.controlCount = controlCount;
	}
}
