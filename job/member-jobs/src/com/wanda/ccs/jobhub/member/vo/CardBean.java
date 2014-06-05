/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

/**
 * @author YangJianbin
 * 
 */
public class CardBean implements Serializable {
	private static final long serialVersionUID = 222683077927945522L;
	private Long cardId = null;
	private Long orderID = null;
	private Long cinemaId = null;
	private String cardNumber = null;
	private String status = null;

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 */
	public CardBean() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return cinemaId+"|"+cardNumber+"|"+status;
	}
}
