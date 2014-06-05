package com.wanda.member.upgrade.service;

import java.math.BigDecimal;

import com.wanda.member.upgrade.data.TMemberLevel;

public interface MemberLevelService {
	public TMemberLevel getMemberLevel(BigDecimal memberId);

	public void doUpdateMemberLevel(TMemberLevel memberLevel);
}
