package com.wanda.member.countermgt.service;

import com.wanda.member.upgrade.data.TMember;

public interface CounterComputeFacade {
	public void computeByMember(String memberId);
	public void computeByMember(TMember re);
	public void computeCounterbyMemberSet();
	public void setCounter(String memberid, String cinemaId);
}
