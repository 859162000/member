package com.wanda.member.upgrade.service;

import java.math.BigDecimal;

import com.wanda.member.upgrade.data.TPointHistory;

public interface MemberPointService {
	public TPointHistory getThisYearPointHistyByMemberId(String year, BigDecimal memeberid);

	public TPointHistory getAllYearPointHistyByMemberId(String year, BigDecimal memberId);
}
