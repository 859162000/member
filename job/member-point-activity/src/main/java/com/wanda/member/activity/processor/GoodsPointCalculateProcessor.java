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
import com.wanda.member.activity.model.ActivityPointGoods;
import com.wanda.member.activity.model.ActivityPointUpdataParameter;

public class GoodsPointCalculateProcessor implements ItemProcessor<ActivityPointGoods, ActivityPointUpdataParameter>{
	private static final Log logger = LogFactory.getLog(GoodsPointCalculateProcessor.class);
	private StepExecution stepExecution;
	@Override
	public ActivityPointUpdataParameter process(ActivityPointGoods item) throws Exception {
		
		int pointPercent = item.getPointPercent();//按百分比
		int pointAddition = item.getPointAddition();//按固定积分
		BigDecimal saleAmount = item.getSaleAmount();//卖品交易金额
		int activityPoint = 0;//特殊积分
		//判断如果固定值不为0 按照固定值方式增加积分,否则按照百分比方式计算积分
		if(pointAddition != 0){
			activityPoint = pointAddition;
		}else{
			BigDecimal pointPercentBd = new BigDecimal(pointPercent/100.0).setScale(2, BigDecimal.ROUND_HALF_UP);//卖品活动送积分百分比
			activityPoint = saleAmount.multiply(pointPercentBd).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		//如果最终计算结果为0 ，过滤掉数据，不进行积分历史插入等操作
		if(activityPoint == 0){
			return null;
		}
		ActivityPointUpdataParameter param = new ActivityPointUpdataParameter();
		param.setRuleType(RuleTypeConts.GOODS_RULE);
		param.setActivityPoint(activityPoint);
		param.setAdjResion("特殊积分计算:卖品计算 订单ID:"+item.getCsOrderCode()+" 品项ID: "+item.getItemCode());
		param.setExtPointCriteriaId(item.getExtPointCriteriaId());
		param.setExtPointRuleId(item.getExtPointRuleId());
		param.setInnerCode(item.getInnerCode());
		param.setItem(item.getItemCode());
		param.setMemberKey(item.getMemberKey());
		param.setOrderCode(item.getCsOrderCode());
		param.setSetTime(item.getBizDate());
		param.setTransType("con");
		
		param.setBatchId(RuleTypeConts.GOODS_RULE + "_" + stepExecution.getVersion());
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
		logger.info("特殊积分计算--卖品计算,执行状态:"+stepExecution.getStatus()+",处理总数:"+stepExecution.getWriteCount()+",提交次数:"+stepExecution.getCommitCount()+",用时:"+runTime+" ms");
	}
	
}
