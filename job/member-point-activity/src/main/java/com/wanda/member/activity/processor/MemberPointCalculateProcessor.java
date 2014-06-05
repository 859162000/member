package com.wanda.member.activity.processor;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointMember;
import com.wanda.member.activity.model.ActivityPointUpdataParameter;

public class MemberPointCalculateProcessor implements ItemProcessor<ActivityPointMember, ActivityPointUpdataParameter>{
	private static final Log logger = LogFactory.getLog(MemberPointCalculateProcessor.class);
	private StepExecution stepExecution;
	@Override
	public ActivityPointUpdataParameter process(ActivityPointMember item) throws Exception {
		
		int activityPoint = item.getPointAdditionCode();//增加积分额
		ActivityPointUpdataParameter param = new ActivityPointUpdataParameter();
		param.setRuleType(RuleTypeConts.MEMBER_RULE);
		param.setActivityPoint(activityPoint);
		param.setAdjResion("特殊积分计算:会员计算");
		param.setExtPointCriteriaId(item.getExtPointCriteriaId());
		param.setExtPointRuleId(item.getExtPointRuleId());
		param.setMemberKey(item.getMemberKey());
		param.setSetTime(item.getBizDate());
		param.setTransType("mbr");
		
		param.setBatchId(RuleTypeConts.MEMBER_RULE + "_" + stepExecution.getVersion());
		return param;
	}

	@BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
	
	@AfterStep
	public void afterStep(StepExecution stepExecution){
		long endTime = new Date().getTime();
		long startTime = stepExecution.getStartTime().getTime();
		long runTime = endTime - startTime;
		logger.info("特殊积分计算--会员计算,执行状态:"+stepExecution.getStatus()+",处理总数:"+stepExecution.getWriteCount()+",提交次数:"+stepExecution.getCommitCount()+",用时:"+runTime+" ms");
	}
}
