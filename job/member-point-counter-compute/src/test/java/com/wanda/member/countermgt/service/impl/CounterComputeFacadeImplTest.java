package com.wanda.member.countermgt.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.member.countermgt.service.CounterComputeFacade;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class CounterComputeFacadeImplTest {
	@Resource
	CounterComputeFacade  counterComputeFacade = null;
	@Test
	public void testComputeCounterbyMemberSet() {
		counterComputeFacade.computeCounterbyMemberSet();
	}

}
