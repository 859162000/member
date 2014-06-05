package com.wanda.member.countermgt.service;

import java.math.BigDecimal;
import java.util.List;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.data.TDConMember;
import com.wanda.member.upgrade.data.TMember;

public interface CounterComputeServcie {
	
	public int updateMemberInfo(TMember member, RuleChain chain) throws Exception;
	
	/**
	 * 取全部影票数中影票数最大值所在影城
	 * 按银城获取票房书
	 */
	public List<TCinemaTicket> getTicketCountCatalogByClima(BigDecimal memberid);
	/**
	 * 取会员在每家影城积分兑换的全部影票数量
	 * @param bigDecimal
	 * @return
	 */
	
	public List<TCinemaTicket> getPointExchangeByCinema(BigDecimal memberid);
	
	/**
	 * 取购买的影票最近一张影票
	 * @return
	 */
	public TCinemaTicket getTicketLastBuy(BigDecimal memberid);
	/**
	 * 取购买的影票最近一张影票
	 * @return
	 */
	public List<TCinemaTicket> getTicketLastBuys(BigDecimal memberid);
	
	
	public List<TCinemaTicket> getLastPointExchangeTickets(BigDecimal memberid);
	/**
	 * 取会员积分兑换影票最近一张影票
	 * @param memberid
	 * @return
	 */
	public TCinemaTicket getLastPointExchangeTicket(BigDecimal memberid);
	
	public TMember getMember(String memberNo);
	
	public List<TDConMember> getDConMembers(String memberNo);
}
