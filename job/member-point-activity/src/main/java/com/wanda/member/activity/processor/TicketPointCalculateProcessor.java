package com.wanda.member.activity.processor;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointTicket;
import com.wanda.member.activity.model.ActivityPointUpdataParameter;

public class TicketPointCalculateProcessor implements ItemProcessor<ActivityPointTicket, ActivityPointUpdataParameter>{
	private static final Log logger = LogFactory.getLog(TicketPointCalculateProcessor.class);
	private StepExecution stepExecution;
	@Override
	public ActivityPointUpdataParameter process(ActivityPointTicket item) throws Exception {
		
		int pointPercent = item.getPointPercent();//按百分比
		int pointAddition = item.getPointAddition();//按固定积分
		BigDecimal saleAmount = item.getAdmissions();//影票金额
		int activityPoint = 0;//特殊积分
		//判断如果固定值不为0 按照固定值方式增加积分,否则按照百分比方式计算积分
		if(pointAddition != 0){
			activityPoint = pointAddition;
		}else{
			BigDecimal pointPercentBd = new BigDecimal(pointPercent/100.0).setScale(2, BigDecimal.ROUND_HALF_UP);//影票活动送积分百分比
			activityPoint = saleAmount.multiply(pointPercentBd).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		//如果最终计算结果为0 ，过滤掉数据，不进行积分历史插入等操作
		if(activityPoint == 0){
			return null;
		}
		ActivityPointUpdataParameter param = new ActivityPointUpdataParameter();
		param.setRuleType(RuleTypeConts.TICKET_RULE);
		param.setActivityPoint(activityPoint);
		param.setAdjResion("特殊积分计算:影票计算 订单ID:"+item.getCtOrderCode()+" 票号: "+item.getTicketNumber());
		param.setExtPointCriteriaId(item.getExtPointCriteriaId());
		param.setExtPointRuleId(item.getExtPointRuleId());
		param.setInnerCode(item.getInnerCode());
		param.setItem(item.getTicketNumber());
		param.setMemberKey(item.getMemberKey());
		param.setOrderCode(item.getCtOrderCode());
		param.setSetTime(item.getBizDate());
		param.setTransType("ticket");
		
		param.setBatchId(RuleTypeConts.TICKET_RULE + "_" + stepExecution.getVersion());
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
		logger.info("特殊积分计算--影票计算,执行状态:"+stepExecution.getStatus()+",处理总数:"+stepExecution.getWriteCount()+",提交次数:"+stepExecution.getCommitCount()+",用时:"+runTime+" ms");
	}
}
