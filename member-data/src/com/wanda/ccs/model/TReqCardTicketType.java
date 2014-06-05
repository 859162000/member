package com.wanda.ccs.model;

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

import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * TReqCardTicketType generated by hbm2java
 */
@Entity
@Table(name = "T_REQ_CARD_TICKET_TYPE")
public class TReqCardTicketType extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 8690913195566721597L;
	
	private Long id;
	private Long cardTypeId;
	private Long reqTicketTypeId;
	private TReqTicketType tReqTicketType;
	private TCardType tCardType;
	
	public TReqCardTicketType(){
	}
	
	public TReqCardTicketType(TReqTicketType tReqTicketType, Long cardTypeId){
		this.tReqTicketType = tReqTicketType;
		this.reqTicketTypeId = tReqTicketType.getId();
		this.cardTypeId = cardTypeId;
	}
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_REQ_CARD_TICKET_TYPE", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "REQ_CARD_TICKET_TYPE_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CARD_TYPE_ID", nullable = false)
	public Long getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CARD_TYPE_ID", nullable = false, insertable = false, updatable = false)
	public TCardType gettCardType() {
		return tCardType;
	}

	public void settCardType(TCardType tCardType) {
		this.tCardType = tCardType;
	}

	@Column(name = "REQ_TICKET_TYPE_ID", nullable = false)
	public Long getReqTicketTypeId() {
		return reqTicketTypeId;
	}

	public void setReqTicketTypeId(Long reqTicketTypeId) {
		this.reqTicketTypeId = reqTicketTypeId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQ_TICKET_TYPE_ID", nullable = false, insertable = false, updatable = false)
	public TReqTicketType gettReqTicketType() {
		return tReqTicketType;
	}

	public void settReqTicketType(TReqTicketType tReqTicketType) {
		this.tReqTicketType = tReqTicketType;
	}
}
