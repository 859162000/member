package com.wanda.member.countermgt.service.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.upgrade.data.TMember;
@Service("nullCounterRule")
@Scope("prototype")
public class NullCounterRule implements CounterComputeRule {
	Log logger = LogFactory.getLog(NullCounterRule.class);
	@Override
	public void compute(TMember memeber,RuleChain chain) {
		chain.add(this);
		logger.info("[no counter] memeber " + memeber.getMobile() +" has not counter");
		logger.info("[Rule China]  " + chain.printChain()+" has not counter");
	}
	@Override
	public String getRuleId() {
		return "7";
	}
}
