/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;

/**
 * @author YangJianbin
 * 
 */
public class ContactHistoryBean implements Serializable {
	private static final long serialVersionUID = 222683077927945522L;
	private Long memberId = null;
	private Long actRargetId = null;
	private String hasSend = "0";
	private String isControlset;


	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getActRargetId() {
		return actRargetId;
	}

	public void setActRargetId(Long actRargetId) {
		this.actRargetId = actRargetId;
	}

	public String getHasSend() {
		return hasSend;
	}

	public void setHasSend(String hasSend) {
		this.hasSend = hasSend;
	}

	public String getIsControlset() {
		return isControlset;
	}

	public void setIsControlset(String isControlset) {
		this.isControlset = isControlset;
	}
	
	/**
	 * 
	 */
	public ContactHistoryBean() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return actRargetId+"|"+memberId;
	}
}
