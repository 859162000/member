package com.wanda.member.activity.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;

import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRuleSql;

public class RuleToSqlWriter implements ItemWriter<ActivityPointRuleSql> {
	private static final Log logger = LogFactory.getLog(RuleToSqlWriter.class);
	private StepExecution stepExecution;
	private final String CONTEXT_KEY_SUFFIX = "_ACTIVITY_POINT_SQL_LIST";
	@Override
	public void write(List<? extends ActivityPointRuleSql> items) throws Exception {
		
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		
		List<ActivityPointRuleSql> memberSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.MEMBER_RULE + CONTEXT_KEY_SUFFIX);
		List<ActivityPointRuleSql> ticketSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.TICKET_RULE + CONTEXT_KEY_SUFFIX);
		List<ActivityPointRuleSql> goodsSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.GOODS_RULE + CONTEXT_KEY_SUFFIX);
		
		if(null == memberSqlList){
			memberSqlList = new ArrayList<ActivityPointRuleSql>();
		}
		if(null == ticketSqlList){
			ticketSqlList = new ArrayList<ActivityPointRuleSql>();
		}
		if(null == goodsSqlList){
			goodsSqlList = new ArrayList<ActivityPointRuleSql>();
		}
		
		for (ActivityPointRuleSql ruleSql : items) {
			String ruleType = ruleSql.getRuleType();
			logger.debug("RULE_ID： "+ruleSql.getExtPointRuleId()+" 规则类型：" + ruleType + ", 规则SQL语句：" + "规则SQL语句：" + ruleSql.getSqlStr() + "  SQL语句参数：" + ruleSql.getSqlParams().toString());
			if(RuleTypeConts.MEMBER_RULE.equals(ruleType)){
				memberSqlList.add(ruleSql);
			}
			
			if(RuleTypeConts.TICKET_RULE.equals(ruleType)){
				ticketSqlList.add(ruleSql);
			}
			
			if(RuleTypeConts.GOODS_RULE.equals(ruleType)){
				goodsSqlList.add(ruleSql);
			}
		}
		
		stepContext.put(RuleTypeConts.MEMBER_RULE + CONTEXT_KEY_SUFFIX, memberSqlList);
		stepContext.put(RuleTypeConts.TICKET_RULE + CONTEXT_KEY_SUFFIX, ticketSqlList);
		stepContext.put(RuleTypeConts.GOODS_RULE + CONTEXT_KEY_SUFFIX, goodsSqlList);
	}
	
	@BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
	
	@AfterStep
	public void afterStep(StepExecution stepExecution){
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		List<ActivityPointRuleSql> memberSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.MEMBER_RULE + CONTEXT_KEY_SUFFIX);
		List<ActivityPointRuleSql> ticketSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.TICKET_RULE + CONTEXT_KEY_SUFFIX);
		List<ActivityPointRuleSql> goodsSqlList = (List<ActivityPointRuleSql>)stepContext.get(RuleTypeConts.GOODS_RULE + CONTEXT_KEY_SUFFIX);
		
		if(!CollectionUtils.isEmpty(memberSqlList)){
			logger.info("会员规则数量："+memberSqlList.size());
		}else{
			logger.info("没有需要计算的会员规则");
		}
		
		if(!CollectionUtils.isEmpty(ticketSqlList)){
			logger.info("影票规则数量："+ticketSqlList.size());
		}else{
			logger.info("没有需要计算的影票规则");
		}
		
		if(!CollectionUtils.isEmpty(goodsSqlList)){
			logger.info("卖品规则数量："+goodsSqlList.size());
		}else{
			logger.info("没有需要计算的卖品规则");
		}
	}
}
