package com.wanda.member.countermgt.service.rule;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.upgrade.data.TMember;
@Service("setCounterRule")
@Scope("prototype")
public class SetCounterRule implements CounterComputeRule {
	Log log = LogFactory.getLog(SetCounterRule.class);
	@Resource
	protected CounterComputeServcie counterComputeServcie = null;
	@Override
	public void compute(TMember member,RuleChain chain) throws Exception {
		chain.add(this);
		log.info("update memberInfo :" + member);
		log.info("update chain :" + chain.printChain());
		try {
			counterComputeServcie.updateMemberInfo(member,chain);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	@Override
	public String getRuleId() {
		return "8";
	}
}
