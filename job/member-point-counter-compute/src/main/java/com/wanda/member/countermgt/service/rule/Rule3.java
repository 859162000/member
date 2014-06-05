package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.RuleCondition;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.member.upgrade.exception.UnSupportLevelException;
@Service("rule3")
@Scope("prototype")
public class Rule3 extends AbstractCounterRule {
	@Resource(name="setCounterRule")
	protected CounterComputeRule setCounterRule = null;
	@Resource(name="rule4")
	CounterComputeRule rule4;
	@Resource(name="rule5")
	CounterComputeRule rule5;
	@Override
	protected void handleMutiple(TMember memeber) {
		currentRule = rule5;
	}

	@Override
	protected void handleNull(TMember memeber) throws Exception {
		throw new UnsupportedOperationException("should be nullRule");
	}

	@Override
	protected void handleUnique(TMember memeber) {
		TCinemaTicket re = counterComputeServcie.getTicketLastBuy(memeber.getMemberKey());
		memeber.setRegistCinemaId(new BigDecimal(re.getCinemaKey()));
		currentRule = setCounterRule;
	}

	@Override
	protected Condition getCondition(TMember memeber) {
		List<TCinemaTicket> re = counterComputeServcie.getTicketLastBuys(memeber.getMemberKey());
		loger.debug(re);
		return genCondition(re);
	}
	@Override
	public String getRuleId() {
		return "4";
	}
}
