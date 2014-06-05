package com.wanda.member.activity.processor;

import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.activity.model.ActivityPointRule;

public class ActivityPointRuleProcessor implements ItemProcessor<ActivityPointRule, ActivityPointRule> {
	
	@Override
	public ActivityPointRule process(ActivityPointRule rule) throws Exception {
		// TODO 需要判断rule的开始结束时间
		return rule;
	}
	
}
