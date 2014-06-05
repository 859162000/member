package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.member.upgrade.data.TMemberMapper;
import com.wanda.mms.dao.MyBatisDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class RuleTest {
	@Resource
	protected Rule1 rule1 = null;	
	@Resource
	protected Rule2 rule2 = null;	
	@Resource
	protected Rule3 rule3 = null;	
	@Resource
	protected Rule4 rule4 = null;
	@Resource
	protected Rule5 rule5 = null;
	@Resource
	protected SetCounterRule setCounterRule = null;
	TMember member = new TMember();
	RuleChain chain = null;
	protected CounterComputeServcie counterComputeServcie = null;
	@Resource
	MyBatisDAO myBatisDAO = null;
	protected SqlSession sqlSession = null;
	TMemberMapper tmemberMapper = null;
	@Before
	public void setup(){
		sqlSession = myBatisDAO.sqlSession;
		member.setMemberId(new BigDecimal("10000"));
		member.setMemberNo("10000");
		member.setMemberKey(new BigDecimal("10000"));
		chain = new RuleChain();
		counterComputeServcie =EasyMock.createMock(CounterComputeServcie.class);
		tmemberMapper = EasyMock.createMock(TMemberMapper.class);
		ReflectionTestUtils.setField(rule1, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		ReflectionTestUtils.setField(rule1, "setCounterRule", setCounterRule, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule2, "setCounterRule", setCounterRule, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule3, "setCounterRule", setCounterRule, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule2, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		ReflectionTestUtils.setField(rule3, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		
		ReflectionTestUtils.setField(rule4, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		ReflectionTestUtils.setField(rule5, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		ReflectionTestUtils.setField(setCounterRule, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
		ReflectionTestUtils.setField(rule1, "rule2", rule2, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule2, "rule3", rule3, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule2, "rule4", rule4, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule3, "rule4", rule4, CounterComputeRule.class);
		ReflectionTestUtils.setField(rule4, "rule5", rule5, CounterComputeRule.class);
		
	}
}
