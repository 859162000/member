package com.wanda.member.countermgt.service.rule;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.exception.UnRuleException;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.RuleCondition;
import com.wanda.member.upgrade.data.TMember;
@Service("rule4")
@Scope("prototype")
public class Rule4 extends AbstractCounterRule {
	@Resource(name="rule5")
	protected CounterComputeRule rule5 = null;
	@Resource(name="nullCounterRule")
	protected CounterComputeRule nullCounterRule = null;
	@Resource(name="setCounterRule")
	protected CounterComputeRule setCounterRule = null;
	
	@Override
	protected void handleMutiple(TMember memeber) {
		currentRule = rule5;
	}

	@Override
	protected void handleNull(TMember memeber) throws Exception {
		throw new UnRuleException("handle null should be set");
	}

	@Override
	protected void handleUnique(TMember memeber) {
		currentRule = setCounterRule;
	}

	@Override
	protected Condition getCondition(TMember memeber) {
		List<TCinemaTicket> list  = counterComputeServcie.getLastPointExchangeTickets(memeber.getMemberId());
		return genCondition(list);
	}
	@Override
	public String getRuleId() {
		return "4";
	}
}
