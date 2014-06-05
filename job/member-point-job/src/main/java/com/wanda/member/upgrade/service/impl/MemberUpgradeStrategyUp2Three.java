package com.wanda.member.upgrade.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.service.AbstractMemberUpgradeStratrey;

@Service("memberUpgradeStrategyUp2Three")
@Scope("prototype")
public class MemberUpgradeStrategyUp2Three extends AbstractMemberUpgradeStratrey{
	
	protected boolean isNeedUpgrade(int level_point, int ticket_count) {
		return ticket_count>=24||level_point>=1000;
	}
	
	@Override
	protected int getMemberLevel() {
		return 3;
	}
	

	@Override
	protected TPointHistory getPointHistory(String year, TMemberLevel memberLevel) {
		return memberPointService.getThisYearPointHistyByMemberId(year, memberLevel.getMemberId());
	}

	@Override
	protected int getTicketLevelCount() {
		return 48;
	}

	@Override
	public int getLevelPoint() {
		return 3000;
	}
}
