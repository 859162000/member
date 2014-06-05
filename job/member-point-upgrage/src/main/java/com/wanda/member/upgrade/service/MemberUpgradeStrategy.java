package com.wanda.member.upgrade.service;

import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TPointHistory;

public interface MemberUpgradeStrategy {

	long compute(String year, TMemberLevel memberLevel);
	
}
