package com.wanda.member.countermgt.service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.upgrade.data.TMember;

public interface CounterComputeRule {
	void compute(TMember memeber,RuleChain chain) throws Exception;
	String getRuleId();
}
