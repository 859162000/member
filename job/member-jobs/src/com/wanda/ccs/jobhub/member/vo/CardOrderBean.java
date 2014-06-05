/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

/**
 * @author YangJianbin
 * 
 */
public class CardOrderBean implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -6978282855691370768L;

	private Long orderID = null;
	private Long cinemaId = null;
	private String regionCode = null;
	private Long numberOfCards = null;
	private String startNo = null;
	private String endNo = null;

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

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Long getNumberOfCards() {
		return numberOfCards;
	}

	public void setNumberOfCards(Long numberOfCards) {
		this.numberOfCards = numberOfCards;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getEndNo() {
		return endNo;
	}

	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}

	/**
	 * 
	 */
	public CardOrderBean() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return regionCode+"|"+cinemaId+"|"+startNo+"|"+numberOfCards;
	}
}
