package com.wanda.member.upgrade.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.service.MemberHelper;
import com.wanda.member.upgrade.service.MemberPointService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class MemberPointServiceImplTest {
	@Resource
	MemberPointService memberPointService = null;
	@Test
	public void test() {
		BigDecimal memeberid = new BigDecimal("16259");
		TPointHistory re = memberPointService.getThisYearPointHistyByMemberId("2013", memeberid );
		Assert.assertEquals(new BigDecimal(630), re.getLevelPoint());
	}

}
