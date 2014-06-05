package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.TestCase;
import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.upgrade.data.TMember;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class TestCounterComputeByDB {
	@Resource
	protected Rule1 rule1 = null;
//	@Test
	@TestCase(id="20000",description="1->8:member_id :16359:C0000000000000016351", creater = "Li Ning")
	public void test_001(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("16359"));
		memeber.setMemberNo("C0000000000000016351");
		try {
			rule1.compute(memeber , chain);
			System.out.println(chain.printChain());
//			Assert.assertTrue(chain.printChain().contains((SetCounterRule.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1->2->8:member_id :19401:C0000000000000019393", creater = "Li Ning")
	public void test_002(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("19401"));
		memeber.setMemberNo("C0000000000000019393");
		try {
			rule1.compute(memeber , chain);
			System.out.println(chain.printChain());
			Assert.assertTrue(chain.printChain().contains(Rule2.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1->2->3->8:member_id :19408:C0000000000000019400", creater = "Li Ning")
	public void test_003(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("19408"));
		memeber.setMemberNo("C0000000000000019400");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_003"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains(Rule2.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1->2->3->5->8:member_id :19451 C0000000000000019443", creater = "Li Ning")
	public void test_004(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("19451"));
		memeber.setMemberNo("C0000000000000019443");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_004"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains(Rule5.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
//	@Test
	@TestCase(id="20000",description="1->2->4->8:19458  C0000000000000019450", creater = "Li Ning")
	public void test_005(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("19458"));
		memeber.setMemberNo("C0000000000000019450");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_005"+chain.printChain());
			Assert.assertTrue(!chain.printChain().contains(Rule4.class.getName()));
			Assert.assertTrue(chain.printChain().contains(Rule2.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1==>2==>4==>5==>8:member_key=24018  C0000000000000024010", creater = "Li Ning")
	public void test_006(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("24018"));
		memeber.setMemberNo("C0000000000000024010");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_006"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains(Rule4.class.getName()));
			Assert.assertTrue(chain.printChain().contains(Rule5.class.getName()));
			Assert.assertTrue(chain.printChain().contains(Rule2.class.getName()));
			Assert.assertTrue(chain.printChain().endsWith(SetCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1==>7:member_key=6129039  C0000000000006129031", creater = "Li Ning")
	public void test_007(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("6129039"));
		memeber.setMemberNo("C0000000000006129031");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_006"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains(NullCounterRule.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
//	@Test
	@TestCase(id="20000",description="1==>2==>4==>5==>6==>8:member_key=24077 C0000000000000024069", creater = "Li Ning")
	public void test_008(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("24077"));
		memeber.setMemberNo("C0000000000000024069");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_006"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains(Rule5.class.getName()));
			Assert.assertTrue(chain.printChain().contains(Rule6.class.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	@TestCase(id="20000",description="1==>8:member_key=4899020 C0000000000004899012", creater = "Li Ning")
	public void test_009(){
		RuleChain chain =new RuleChain();
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal("4899020"));
		memeber.setMemberNo("C0000000000004899012");
		try {
			rule1.compute(memeber , chain);
			System.out.println("test_006"+chain.printChain());
			Assert.assertTrue(chain.printChain().contains((new SetCounterRule()).getRuleId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
			
}
