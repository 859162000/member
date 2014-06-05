package com.wanda.member.upgrade.service.impl;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TMember;
import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TMemberMapper;
import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.service.AbstractMemberUpgradeStratrey;
import com.wanda.member.util.DateUtil;

@Service("memberUpgradeStrategyUp2Two")
@Scope("prototype")
public class MemberUpgradeStrategyUp2Two extends AbstractMemberUpgradeStratrey{
	
	protected boolean isNeedUpgrade(int level_point, int ticket_count) {
		return ticket_count>=12||level_point>=500;
	}

	@Override
	protected int getMemberLevel() {
		return 2;
	}

	public int getLevelPoint() {
		return 500;
	}

	@Override
	protected TPointHistory getPointHistory(String year, TMemberLevel memberLevel) {
		TMemberMapper mapper = sqlSession.getMapper(TMemberMapper.class);
		TMember member = mapper.selectByPrimaryKeyByMemberId(memberLevel.getMemberId());
		Date when = DateUtil.getFirstDayofYear(Integer.parseInt(year)-1);
		boolean isOverTime = member.getCreateDate().after(when);//判断是否垮了2年
		if(isOverTime){
			return memberPointService.getAllYearPointHistyByMemberId(year, memberLevel.getMemberId());
		}else{
			return memberPointService.getThisYearPointHistyByMemberId(year, memberLevel.getMemberId());
		}
		
	}
	@Test
	public void testGetRemainPoint(){
		long re = getRemainPoint(7200);
		Assert.assertEquals(0, re);
	}
	@Test
	public void testIsNeedUpgrade(){
		boolean re = isNeedUpgrade(0,1000);
		Assert.assertTrue(re);
	}

	@Override
	protected int getTicketLevelCount() {
		return 24;
	}
}
