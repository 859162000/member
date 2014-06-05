package com.wanda.member.activity.model;

import java.util.List;

public class ActivityPointRuleSql {
	private String ruleType;
	private int extPointRuleId;
    private int extPointCriteriaId;
    private int additionPercent;
    private int additionCode;
    private String sqlStr;
    private List<Object> sqlParams;
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
	public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}
	public List<Object> getSqlParams() {
		return sqlParams;
	}
	public void setSqlParams(List<Object> sqlParams) {
		this.sqlParams = sqlParams;
	}
    
    
}
