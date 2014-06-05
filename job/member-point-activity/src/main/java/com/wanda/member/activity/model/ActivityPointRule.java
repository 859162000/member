package com.wanda.member.activity.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "rule")
public class ActivityPointRule {
	private String ruleType;
    private int extPointRuleId;
    private int extPointCriteriaId;
    private String ruleCode;
    private String ruleName;
    private Date startDtime;
    private Date endDtime;
    private int additionPercent;
    private int additionCode;
    private String criteriaCode;
    private String criteriaName;
    private String criteriaScheme;
    
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public int getExtPointRuleId() {
		return extPointRuleId;
	}
	public void setExtPointRuleId(int extPointRuleId) {
		this.extPointRuleId = extPointRuleId;
	}
	public int getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(int extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Date getStartDtime() {
		return startDtime;
	}
	public void setStartDtime(Date startDtime) {
		this.startDtime = startDtime;
	}
	public Date getEndDtime() {
		return endDtime;
	}
	public void setEndDtime(Date endDtime) {
		this.endDtime = endDtime;
	}
	public int getAdditionPercent() {
		return additionPercent;
	}
	public void setAdditionPercent(int additionPercent) {
		this.additionPercent = additionPercent;
	}
	public int getAdditionCode() {
		return additionCode;
	}
	public void setAdditionCode(int additionCode) {
		this.additionCode = additionCode;
	}
	public String getCriteriaCode() {
		return criteriaCode;
	}
	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}
	public String getCriteriaName() {
		return criteriaName;
	}
	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}
	public String getCriteriaScheme() {
		return criteriaScheme;
	}
	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}
  
}