package com.wanda.ccs.model;

// Generated 2011-11-10 19:35:25 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Arrays;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.xcesys.extras.core.dao.model.BlameableEntity;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.ConvertUtil;

/**
 * 连场子场次实体类，与场次实体类(T_SCHEDULE_PLAN_B)为N:1关系。
 * 
 * @author xiaofeng
 *
 */
@Entity
@Table(name = "T_SUB_ROUND_TICKET_TYPE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TSubRoundTicketType extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = -3001856158632897043L;
	
	private Long id;
	private TRoundFilm tRoundFilm;
	private TPriceBase origPrice;
	private Long origPriceId;
	private Long adjustPriceId;
	private TPriceBase adjustPrice;
	private TTicketType tTicketType;
	private Long exchangeNum;
	private Long adjustAmount;
	private Boolean enabled;
	private String reason;
	private String block;
	private Boolean needAuthorize;
	private Boolean memberDiscount;
	private Long points;
	private Boolean standardPrice;
	private Boolean lowestPrice;
	private String channel;
	private String[] channels;
	private String ticketTypeName;
	/** 根据票价规则计算得出的价格 */
	private Integer price;

	public TSubRoundTicketType() {
	}
	
	public TSubRoundTicketType(TRoundTicketType rtt) {
		this.adjustAmount = rtt.getAdjustAmount();
		this.adjustPriceId = rtt.getAdjustPriceId();
		this.adjustPrice = rtt.getAdjustPrice();
		this.block = rtt.getBlock();
		this.channel = rtt.getChannel();
		this.enabled = rtt.getEnabled();
		this.exchangeNum = rtt.getExchangeNum();
		this.lowestPrice = rtt.getLowestPrice();
		this.memberDiscount = rtt.getMemberDiscount();
		this.needAuthorize = rtt.getNeedAuthorize();
		this.origPriceId = rtt.getOrigPriceId();
		this.origPrice = rtt.getOrigPrice();
		this.points = rtt.getPoints();
		this.reason = rtt.getReason();
		this.standardPrice = rtt.getStandardPrice();
		this.ticketTypeName = rtt.getTicketTypeName();
		this.tTicketType = rtt.gettTicketType();
	}

	@Column(name = "ADJUST_AMOUNT", precision = 38, scale = 0)
	public Long getAdjustAmount() {
		return this.adjustAmount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADJUST_PRICE_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TPriceBase getAdjustPrice() {
		return adjustPrice;
	}

	@Column(name = "ADJUST_PRICE_ID")
	public Long getAdjustPriceId() {
		return adjustPriceId;
	}

	@Column(name = "BLOCK", length = 2)
	public String getBlock() {
		return block;
	}

	@Column(name = "CHANNEL", length = 50)
	public String getChannel() {
		return channel;
	}

	@Transient
	public String[] getChannels() {
		if (!StringUtil.isNullOrBlank(this.channel)) {
			this.channels = this.channel.split(",");
			Arrays.sort(this.channels);
		}
		return channels;
	}

	@Column(name = "ENABLED", length = 1)
	public Boolean getEnabled() {
		return this.enabled;
	}

	@Column(name = "EXCHANGE_NUM", precision = 38, scale = 0)
	public Long getExchangeNum() {
		return this.exchangeNum;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_SUB_ROUND_TICKET_TYPE")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	@Column(name = "LOWEST_PRICE")
	public Boolean getLowestPrice() {
		return lowestPrice;
	}

	@Column(name = "MEMBER_DISCOUNT")
	public Boolean getMemberDiscount() {
		return memberDiscount;
	}
	
	@Column(name = "POINTS")
	public Long getPoints() {
		return points;
	}

	@Column(name = "TICKET_TYPE_NAME", nullable = false, length = 20)
	public String getTicketTypeName() {
		return this.ticketTypeName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORIG_PRICE_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TPriceBase getOrigPrice() {
		return this.origPrice;
	}

	@Column(name = "ORIG_PRICE_ID")
	public Long getOrigPriceId() {
		return origPriceId;
	}

	/**
	 * 返回表示价格是否被改动的标记。
	 * 
	 * @return
	 */
	@Transient
	public boolean getPriceModified() {
		if (getOrigPrice() != null && getAdjustPrice() != null) {
			return this.origPrice.getPrice() != this.adjustPrice.getPrice();
		}
		return false;
	}

	@Column(name = "REASON", length = 100)
	public String getReason() {
		return this.reason;
	}

	@Column(name = "STANDARD_PRICE")
	public Boolean getStandardPrice() {
		return standardPrice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "T_ROUND_FILM_ID")
	public TRoundFilm gettRoundFilm() {
		return this.tRoundFilm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "T_TICKET_TYPE_ID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TTicketType gettTicketType() {
		return this.tTicketType;
	}
	
	@Column(name="NEED_AUTHORIZE")
	public Boolean getNeedAuthorize() {
		return needAuthorize;
	}

	public void setAdjustAmount(Long adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public void setAdjustPrice(TPriceBase adjustPrice) {
		this.adjustPrice = adjustPrice;
	}

	public void setAdjustPriceId(Long adjustPriceId) {
		this.adjustPriceId = adjustPriceId;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setChannels(String[] channels) {
		this.channels = channels;
		if (this.channels != null) {
			Arrays.sort(channels);
			this.channel = ConvertUtil.convertArrayToCommaString(this.channels);
		}
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setExchangeNum(Long exchangeNum) {
		this.exchangeNum = exchangeNum;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLowestPrice(Boolean lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public void setMemberDiscount(Boolean memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public void setTicketTypeName(String name) {
		this.ticketTypeName = name;
	}

	public void setOrigPrice(TPriceBase tPriceBase) {
		this.origPrice = tPriceBase;
	}

	public void setOrigPriceId(Long origPriceId) {
		this.origPriceId = origPriceId;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setStandardPrice(Boolean standardPrice) {
		this.standardPrice = standardPrice;
	}

	public void settRoundFilm(TRoundFilm tRoundFilm) {
		this.tRoundFilm = tRoundFilm;
	}

	public void settTicketType(TTicketType tTicketType) {
		this.tTicketType = tTicketType;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	public void setNeedAuthorize(Boolean needAuthorize) {
		this.needAuthorize = needAuthorize;
	}

	@Transient
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
}
