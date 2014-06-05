package com.wanda.member.countermgt.service.rule;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.wanda.TestCase;
import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.data.TDConMember;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.exception.NullCinemaException;
public class Rule1Test extends RuleTest{
	@Test
	@TestCase(description="1->2->3->8 ", creater = "lining", id = "20000")
	public void testCompute_001() {
		List list = new ArrayList();
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		EasyMock.expect(counterComputeServcie.getPointExchangeByCinema(member.getMemberId())).andReturn(list );
		EasyMock.expect(counterComputeServcie.getTicketLastBuy(member.getMemberId())).andReturn(new TCinemaTicket());
		List<TDConMember> memberList = new ArrayList<TDConMember>();
		TDConMember tdc = new TDConMember();
		tdc.setMemberId(member.getMemberId());
		tdc.setMemberKey(member.getMemberKey());
		memberList.add(tdc);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		try {
			EasyMock.replay(counterComputeServcie);
			rule1.compute(member,chain);
			Assert.assertEquals(NullCounterRule.class, rule1.currentRule.getClass());
			Assert.assertEquals(2, chain.getRueChain().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	@Test
	@TestCase(description="1->8 :影城编码为空", creater = "lining", id = "20000")
	public void testCompute_002() {
		
		List<TCinemaTicket> list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		List<TDConMember> memberList = new ArrayList<TDConMember>();
		TDConMember tdc = new TDConMember();
		tdc.setMemberId(member.getMemberId());
		tdc.setMemberKey(member.getMemberKey());
		memberList.add(tdc);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		try {
			EasyMock.replay(counterComputeServcie);
			ReflectionTestUtils.setField(rule1, "counterComputeServcie", counterComputeServcie, CounterComputeServcie.class);
			rule1.compute(member,chain);
			Assert.assertEquals(SetCounterRule.class, rule1.currentRule.getClass());
			Assert.assertEquals(2, chain.getRueChain().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(e instanceof NullCinemaException);
		}
	}
	@Test
	@TestCase(description="1->8 :影城编码不为空", creater = "lining", id = "20000")
	public void testCompute_05() {
		RuleChain chain = new RuleChain();
		List<TCinemaTicket> list = new ArrayList<TCinemaTicket>();
		TCinemaTicket t = new TCinemaTicket();
		t.setCinemaKey("54321");
		t.setMemberId(member.getMemberId());
		list.add(t);
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		List<TDConMember> memberList = new ArrayList<TDConMember>();
		TDConMember tdc = new TDConMember();
		tdc.setMemberId(member.getMemberId());
		tdc.setMemberKey(member.getMemberKey());
		memberList.add(tdc);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		try {
			EasyMock.expect(counterComputeServcie.updateMemberInfo(member,chain)).andReturn(0);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Assert.fail(e2.getMessage());
		}
		try {
			EasyMock.replay(counterComputeServcie);
			rule1.compute(member,chain);
			Assert.assertEquals(SetCounterRule.class, rule1.currentRule.getClass());
			Assert.assertEquals(2, chain.getRueChain().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	@Test
	@TestCase(description="1->2->8 ", creater = "lining", id = "20000")
	public void testCompute_003() {
		RuleChain chain = new RuleChain();
		List<TCinemaTicket> list = new ArrayList<TCinemaTicket>();
		TCinemaTicket tt1 = new TCinemaTicket();
		tt1.setCinemaKey("123");
		tt1.setTicketNum(2);
		TCinemaTicket tt2 = new TCinemaTicket();
		tt2.setCinemaKey("1232");
		tt2.setTicketNum(2);
		list.add(tt1);
		list.add(tt2);
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		list = new ArrayList<TCinemaTicket>();
		TCinemaTicket tc = new TCinemaTicket();
		tc.setCinemaKey("123");
		list.add(tc);
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		list = new ArrayList<TCinemaTicket>();
		TCinemaTicket tt = new TCinemaTicket();
		tt.setCinemaKey("123");
		list.add(tt);
		EasyMock.expect(counterComputeServcie.getPointExchangeByCinema    (member.getMemberId())).andReturn(list);
		EasyMock.expect(counterComputeServcie.getPointExchangeByCinema    (member.getMemberId())).andReturn(list);
		try {
			EasyMock.expect(counterComputeServcie.updateMemberInfo(member,chain)).andReturn(0);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Assert.fail(e2.getMessage());
		}
		List<TDConMember> memberList = new ArrayList<TDConMember>();
		TDConMember tdc = new TDConMember();
		tdc.setMemberId(member.getMemberId());
		tdc.setMemberKey(member.getMemberKey());
		memberList.add(tdc);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		try {
			EasyMock.replay(counterComputeServcie);
			rule1.compute(member,chain);
			System.out.println(chain.printChain());
			Assert.assertEquals(Rule2.class, rule1.currentRule.getClass());
			Assert.assertEquals(3, chain.getRueChain().size());
			System.out.println(chain.printChain());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
//	@Test
	@TestCase(description="1->2->4->8 ", creater = "lining", id = "20000")
	public void testCompute_004() {
		List<TCinemaTicket> list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getPointExchangeByCinema    (member.getMemberId())).andReturn(list);
		list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getLastPointExchangeTickets (member.getMemberId())).andReturn(list);
		List<TDConMember> memberList = new ArrayList<TDConMember>();
		TDConMember tdc = new TDConMember();
		tdc.setMemberId(member.getMemberId());
		tdc.setMemberKey(member.getMemberKey());
		memberList.add(tdc);
		EasyMock.expect(counterComputeServcie.getDConMembers(member.getMemberNo())).andReturn(memberList);
		try {
			EasyMock.replay(counterComputeServcie);
			rule1.compute(member,chain);
			Assert.assertEquals(Rule2.class, rule1.currentRule.getClass());
			Assert.assertEquals(4, chain.getRueChain().size());
			Assert.assertTrue(chain.printChain().contains(Rule4.class.getName()));
			System.out.println(chain.printChain());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	//@Test
	@TestCase(description="1->2->4->5->8 ", creater = "lining", id = "20000")
	public void testCompute_005() {
		List<TCinemaTicket> list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getTicketCountCatalogByClima(member.getMemberId())).andReturn(list );
		list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getPointExchangeByCinema    (member.getMemberId())).andReturn(list);
		list = new ArrayList<TCinemaTicket>();
		list.add(new TCinemaTicket());
		list.add(new TCinemaTicket());
		EasyMock.expect(counterComputeServcie.getLastPointExchangeTickets    (member.getMemberId())).andReturn(list);
		EasyMock.expect(counterComputeServcie.getTicketLastBuy(member.getMemberId())).andReturn(new TCinemaTicket());
		
		try {
			EasyMock.replay(counterComputeServcie);
			rule1.compute(member,chain);
			Assert.assertEquals(Rule2.class, rule1.currentRule.getClass());
			Assert.assertEquals(5, chain.getRueChain().size());
			Assert.assertTrue(chain.printChain().contains(Rule4.class.getName()));
			Assert.assertTrue(chain.printChain().contains(Rule5.class.getName()));
			System.out.println(chain.printChain());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
