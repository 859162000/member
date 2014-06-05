package com.wanda.member.activity.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ActivityPointTicketRowMapper implements RowMapper<ActivityPointTicket> {

	@Override
	public ActivityPointTicket mapRow(ResultSet rs, int rownum)
			throws SQLException {
		ActivityPointTicket ticket = new ActivityPointTicket();
		ticket.setInnerCode(rs.getString("INNER_CODE"));
		ticket.setMemberKey(rs.getString("MEMBER_KEY"));
		ticket.setAdmissions(rs.getBigDecimal("ADMISSIONS"));
		ticket.setCtOrderCode(rs.getString("CT_ORDER_CODE"));
		ticket.setTicketNumber(rs.getString("TICKET_NUMBER"));
		return ticket;
	}

}
