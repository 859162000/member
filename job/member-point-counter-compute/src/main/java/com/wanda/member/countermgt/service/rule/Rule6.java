package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.upgrade.data.TMember;
@Service("rule6")
@Scope("prototype")
public class Rule6 extends AbstractCounterRule {
	@Resource(name="setCounterRule")
	protected CounterComputeRule setCounterRule = null;
	
	@Override
	protected void handleMutiple(TMember memeber) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleNull(TMember memeber) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleUnique(TMember memeber) {
		TCinemaTicket re = counterComputeServcie.getLastPointExchangeTicket(memeber.getMemberId());
		memeber.setRegistCinemaId(new BigDecimal(re.getCinemaKey()));
		currentRule = setCounterRule;
	}

	@Override
	protected Condition getCondition(TMember memeber) {
		TCinemaTicket re = counterComputeServcie.getLastPointExchangeTicket(memeber.getMemberId());
		return genCondition(re);
	}
	@Override
	public String getRuleId() {
		return "6";
	}
}
