package com.wanda.member.upgrade.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.TestCase;
import com.wanda.member.upgrade.data.TLevelHistoryMapper;
import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TPointHistory;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")

public class MemberUpgradeStratreyUp2TwoTest {
	@Resource(name="memberUpgradeStrategyUp2Two")
	MemberUpgradeStrategy stratrey = null;
	@Resource
	MemberPointService memberPointService = null;
	
	@Resource
	MemberLevelHisService memberLevelHisService = null;
	@Resource
	MemberLevelService memberLevelService = null;
	TMemberLevel memberLevel =null;
//	BigDecimal memeberid = new BigDecimal("6754491"); 升级的
	
//	BigDecimal memeberid = new BigDecimal("6754492"); // 不升级的
	
	BigDecimal memeberid = null; // 非定级
	
	private long seq = 0;
//	/6754279
	@Before
	public void setup(){
		
	}
	@After
	public void tearDown(){
		memeberid = null;
		memberLevelService.doUpdateMemberLevel(memberLevel);
		memberLevelHisService.doDeleteMemberLevelHis(seq);
		
		
	}
	@Test
	@TestCase(creater="Li Ning",id="10000",description="满足500积分的一星会员升级为二星")
	public void testCompute_10000() {
		memeberid = new BigDecimal("5633055");
		memberLevel = memberLevelService.getMemberLevel(memeberid);
		seq = stratrey.compute("2013", memberLevel);
		TMemberLevel re = memberLevelService.getMemberLevel(memeberid);
		Assert.assertEquals("2", re.getMemLevel());
		
	}
	
}
