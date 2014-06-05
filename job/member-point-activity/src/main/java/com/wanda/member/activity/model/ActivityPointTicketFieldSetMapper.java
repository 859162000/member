package com.wanda.member.activity.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ActivityPointTicketFieldSetMapper implements FieldSetMapper<ActivityPointTicket> {

	@Override
	public ActivityPointTicket mapFieldSet(FieldSet fieldSet)
			throws BindException {
		ActivityPointTicket ticket = new ActivityPointTicket();
		ticket.setBizDate(fieldSet.readString("bizDate"));
		ticket.setCtOrderCode(fieldSet.readString("ctOrderCode"));
		ticket.setExtPointCriteriaId(fieldSet.readInt("extPointCriteriaId"));
		ticket.setExtPointRuleId(fieldSet.readInt("extPointRuleId"));
		ticket.setInnerCode(fieldSet.readString("innerCode"));
		ticket.setTicketNumber(fieldSet.readString("ticketNumber"));
		ticket.setMemberKey(fieldSet.readString("memberKey"));
		ticket.setPointPercent(fieldSet.readInt("pointPercent"));
		ticket.setPointAddition(fieldSet.readInt("pointAddition"));
		ticket.setAdmissions(fieldSet.readBigDecimal("admissions"));
		return ticket;
	}

}
