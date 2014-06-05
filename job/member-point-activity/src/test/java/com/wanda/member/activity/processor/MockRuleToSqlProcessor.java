package com.wanda.member.activity.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;
import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRule;
import com.wanda.member.activity.model.ActivityPointRuleSql;

public class MockRuleToSqlProcessor implements ItemProcessor<ActivityPointRule, ActivityPointRuleSql> {
	private String bizDate;
	@Override
	public ActivityPointRuleSql process(ActivityPointRule item)
			throws Exception {
		String ruleType = item.getRuleType();
		ActivityPointRuleSql ruleSql = new ActivityPointRuleSql();
		BeanUtils.copyProperties(ruleSql, item);
		String criteriaJson = item.getCriteriaScheme();
		List<ExpressionCriterion> criteriaList = JsonCriteriaHelper.parse(criteriaJson);
		//会员类规则
		if(RuleTypeConts.MEMBER_RULE.equals(ruleType)){
			
			ruleSql.setSqlStr("select member_id as member_key from tmp_tb03");
			ruleSql.setSqlParams(new ArrayList<Object>());
		}
		
		//影票类规则
		if(RuleTypeConts.TICKET_RULE.equals(ruleType)){
			//获取影票查询SQL
			ruleSql.setSqlStr("select * from tmp_tb02");
			ruleSql.setSqlParams(new ArrayList<Object>());
		}
		
		//卖品类规则
		if(RuleTypeConts.GOODS_RULE.equals(ruleType)){
			//获取卖品查询SQL
			ruleSql.setSqlStr("select * from tmp_tb01");
			ruleSql.setSqlParams(new ArrayList<Object>());
		}
		
		return ruleSql;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	
}
