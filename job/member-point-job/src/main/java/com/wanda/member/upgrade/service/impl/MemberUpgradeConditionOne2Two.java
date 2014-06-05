package com.wanda.member.upgrade.service.impl;

import com.wanda.member.upgrade.service.MemberUpgradeCondition;

public class MemberUpgradeConditionOne2Two implements MemberUpgradeCondition{
	public MemberUpgradeConditionOne2Two(int level_point, int ticket_count){
		
	}
	@Override
	public boolean isOk() {
		return false;
	}
	
}
