package com.wanda.member.countermgt.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.upgrade.data.TMember;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")

public class CounterComputeServcieImplTest {
	@Resource
	CounterComputeServcie counterComputeServcie = null;
	@Test
	public void testGetTicketCountCatalogByClima() {
		List<TCinemaTicket> re = counterComputeServcie.getTicketCountCatalogByClima(new BigDecimal("2919557"));
		Assert.assertEquals(3, re.size());
	}
	@Test
	public void testgetPointExchangeByCinema() {
		List<TCinemaTicket> re = counterComputeServcie.getPointExchangeByCinema(new BigDecimal("2053669"));
		Assert.assertEquals(0, re.size());
	}
}
