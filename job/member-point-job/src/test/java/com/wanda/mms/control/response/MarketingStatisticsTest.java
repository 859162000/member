package com.wanda.mms.control.response;

import static org.junit.Assert.*;

import org.junit.Test;


import junit.framework.Assert;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class MarketingStatisticsTest {

	@Test
	public void test() {
		 MarketingStatistics.execute();
	}
	/*
	 * t_act_result 核查这个表中的数据
	 */
	public void testResult(){
		
	}
}
