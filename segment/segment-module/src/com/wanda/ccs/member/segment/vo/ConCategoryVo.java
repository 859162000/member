package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;
import java.util.Date;

public class ConCategoryVo implements Serializable {
	
	private static final long serialVersionUID = 5180219532890811757L;
	
	private Long conCategoryId;
	private Long pConCategoryId;
	private String categoryCode;
	private String categoryName;
	private String categoryDesc;
	private String itemType;
	
	
	public ConCategoryVo() {
	}

	public Long getConCategoryId() {
		return conCategoryId;
	}


	public void setConCategoryId(Long conCategoryId) {
		this.conCategoryId = conCategoryId;
	}


	public Long getPConCategoryId() {
		return pConCategoryId;
	}


	public void setPConCategoryId(Long pConCategoryId) {
		this.pConCategoryId = pConCategoryId;
	}


	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getCategoryDesc() {
		return categoryDesc;
	}


	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public Long getpConCategoryId() {
		return pConCategoryId;
	}

	public void setpConCategoryId(Long pConCategoryId) {
		this.pConCategoryId = pConCategoryId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

}
