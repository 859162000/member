package com.wanda.member.countermgt.service;

import com.wanda.member.ObjectType;

public class RuleCondition extends ObjectType implements Condition{
	private boolean isNull = false;
	private boolean isMultiple = false;
	private boolean isUnique = false;
	
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public boolean isNull() {
		return isNull;
	}
	public boolean isMultiple() {
		return isMultiple;
	}
	public boolean isUnique() {
		return isUnique;
	}
	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}
	
	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	
	
}
