package com.wanda.member.countermgt.data;

import java.util.ArrayList;
import java.util.List;

import com.wanda.member.ObjectType;
import com.wanda.member.countermgt.service.CounterComputeRule;

public class RuleChain extends ObjectType{
	private List<CounterComputeRule> list = new ArrayList();
	public void add(CounterComputeRule rule){
		synchronized (rule) {
			list.add(rule);
		}
	}
	
	public List<CounterComputeRule> getRueChain(){
		return list;
	}

	public String printChain() {
		StringBuffer buffer = new StringBuffer(64);
		for(CounterComputeRule rule : list){
			buffer.append("-->");
			buffer.append(rule.getRuleId());
		}
		return buffer.toString();
	}
}
