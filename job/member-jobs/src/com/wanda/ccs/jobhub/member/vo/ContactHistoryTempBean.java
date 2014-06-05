/**
 * 
 */
package com.wanda.ccs.jobhub.member.vo;

import java.io.Serializable;


/**
 * @author YangJianbin
 * 
 */
public class ContactHistoryTempBean implements Serializable {
	private static final long serialVersionUID = 222683077927945522L;
	private Long memberId = null;
	private String mobile = null;
	private Long fileAttachId;
	private Long actRargetId;
	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getFileAttachId() {
		return fileAttachId;
	}

	public void setFileAttachId(Long fileAttachId) {
		this.fileAttachId = fileAttachId;
	}

	public Long getActRargetId() {
		return actRargetId;
	}

	public void setActRargetId(Long actRargetId) {
		this.actRargetId = actRargetId;
	}

	/**
	 * 
	 */
	public ContactHistoryTempBean() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return mobile+"|"+memberId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return this.toString().equals(((ContactHistoryTempBean)obj).toString());
	}
	
	
}
