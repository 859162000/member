package com.wanda.member.upgrade.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.service.AbstractMemberUpgradeStratrey;
@Service("memberUpgradeStratreyUp2Four")
@Scope("prototype")
public class MemberUpgradeStratreyUp2Four extends	AbstractMemberUpgradeStratrey {

	@Override
	protected TPointHistory getPointHistory(String year,TMemberLevel memberLevel) {
		return memberPointService.getThisYearPointHistyByMemberId(year, memberLevel.getMemberId());
	}

	protected int getTicketLevelCount() {
		return 96;
	}
	@Override
	public int getLevelPoint() {
		return 6000;
	}

	@Override
	protected boolean isNeedUpgrade(int level_point, int ticket_count) {
		return ticket_count>=48||level_point>=3000;
	}

	@Override
	protected int getMemberLevel() {
		return 4;
	}

	

}
