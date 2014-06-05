package com.wanda.member.countermgt.data;

import java.math.BigDecimal;
import java.util.List;

public interface TCounterComputeMapper {
	public List<TCinemaTicket> getTicketsCatalogByCinema(BigDecimal memberid);
	public List<TCinemaTicket> getPointExchangeByCinema(BigDecimal memberid);
	public List<TCinemaTicket> getTicketLastBuys(BigDecimal memberid);
	
	public TCinemaTicket getTicketLastBuy(BigDecimal memberid);
	public TCinemaTicket getLastPointExchangeTicket(BigDecimal memberid);
	public List<TCinemaTicket> getLastPointExchangeTickets(BigDecimal memberid);
}