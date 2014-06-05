package com.wanda.ccs.model;

// Generated 2011-11-2 12:36:06 by Hibernate Tools 3.2.4.GA

import com.xcesys.extras.core.dao.model.VersionableEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * TCardTransGood generated by hbm2java
 */
@Entity
@Table(name = "T_CARD_TRANS_GOOD")
public class TCardTransGood extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = -372658437199992594L;
	
	private Long id;
	private TCardTrans tCardTrans;
	private String goodsId;
	private String goodsName;
	private Long goodsCount;
	private Long charge;    

	public TCardTransGood() {
	}

	public TCardTransGood(TCardTrans tCardTrans, Long goodsCount) {
		this.tCardTrans = tCardTrans;
		this.goodsCount = goodsCount;
	}

	public TCardTransGood(TCardTrans tCardTrans, String goodsId,
			String goodsName, Long goodsCount) {
		this.tCardTrans = tCardTrans;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsCount = goodsCount;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_CARD_TRANS_GOOD", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CARD_TRANS_GOOD_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CARD_TRANS_ID")
	public TCardTrans gettCardTrans() {
		return this.tCardTrans;
	}

	public void settCardTrans(TCardTrans tCardTrans) {
		this.tCardTrans = tCardTrans;
	}

	@Column(name = "GOODS_ID", length = 100)
	public String getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	@Column(name = "GOODS_NAME")
	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	@Column(name = "CHARGE", precision = 10 )
	public Long getCharge() {
		return charge;
	}

	public void setCharge(Long charge) {
		this.charge = charge;
	}
	

	@Column(name = "GOODS_COUNT", nullable = false, precision = 38, scale = 0)
	public Long getGoodsCount() {
		return this.goodsCount;
	}

	public void setGoodsCount(Long goodsCount) {
		this.goodsCount = goodsCount;
	}

}