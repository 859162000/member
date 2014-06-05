package com.wanda.member.activity.processor;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.activity.model.ActivityPointTicket;

public class TicketProcessor implements ItemProcessor<ActivityPointTicket, ActivityPointTicket>{
	private static final Log logger = LogFactory.getLog(TicketProcessor.class);
	private int pointPercent;
	private int pointAddition;
	private int extPointRuleId;
	private int extPointCriteriaId;
	private String bizDate;
	@Override
	public ActivityPointTicket process(ActivityPointTicket ticket) throws Exception {
		//票价四舍五入取整
		ticket.setAdmissions(ticket.getAdmissions().setScale(0, BigDecimal.ROUND_HALF_UP));
		//过滤票价小于0的数据
		if(ticket.getAdmissions().intValue() <= 0){
			return null;
		}
		ticket.setPointPercent(pointPercent);
		ticket.setPointAddition(pointAddition);
		ticket.setExtPointRuleId(extPointRuleId);
		ticket.setExtPointCriteriaId(extPointCriteriaId);
		ticket.setBizDate(bizDate);
		return ticket;
	}
	public int getPointPercent() {
		return pointPercent;
	}
	public void setPointPercent(int pointPercent) {
		this.pointPercent = pointPercent;
	}
	public int getExtPointRuleId() {
		return extPointRuleId;
	}
	public void setExtPointRuleId(int extPointRuleId) {
		this.extPointRuleId = extPointRuleId;
	}
	public int getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(int extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	public int getPointAddition() {
		return pointAddition;
	}
	public void setPointAddition(int pointAddition) {
		this.pointAddition = pointAddition;
	}
	
	@AfterStep
	public void afterStep(StepExecution stepExecution){
		long endTime = new Date().getTime();
		long startTime = stepExecution.getStartTime().getTime();
		long runTime = endTime - startTime;
		logger.info("票房交易记录，BIZ_DATE:"+this.getBizDate()+",规则编号："+this.getExtPointRuleId()+",需要处理的票房交易数量:"+stepExecution.getWriteCount()+",用时:"+runTime+" ms");
	}
}
