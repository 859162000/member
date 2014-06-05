package com.wanda.member.countermgt.service.rule;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.mchange.v1.lang.GentleThread;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.service.Condition;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.RuleCondition;
import com.wanda.member.exception.UnNumberTypeException;
import com.wanda.member.upgrade.data.TMember;
@Service("rule2")
@Scope("prototype")
public class Rule2 extends AbstractCounterRule {
	@Resource(name="setCounterRule")
	protected CounterComputeRule setCounterRule = null;
	@Resource(name="rule3")
	protected CounterComputeRule rule3 = null;
	@Resource(name="rule4")
	protected CounterComputeRule rule4 = null;
	private List<TCinemaTicket> list = null;
	@Override
	protected void handleMutiple(TMember memeber) {
		currentRule = rule4;
	}

	@Override
	protected void handleNull(TMember memeber) throws Exception {
		currentRule = rule3;
	}

	@Override
	protected void handleUnique(TMember memeber) throws Exception  {
		loger.debug("list.get(0).getCinemaKey():"+list.get(0).getCinemaKey());
		
		if(list.get(0).getCinemaKey()== null){
			throw new UnNumberTypeException();
		}
		memeber.setRegistCinemaId(new BigDecimal(list.get(0).getCinemaKey()));
		currentRule = setCounterRule;
	}

	@Override
	protected Condition getCondition(TMember memeber) {
		list = counterComputeServcie.getPointExchangeByCinema(memeber.getMemberKey());
		return genCondition(list);
	}

	@Override
	public String getRuleId() {
		return "2";
	}
	
}
