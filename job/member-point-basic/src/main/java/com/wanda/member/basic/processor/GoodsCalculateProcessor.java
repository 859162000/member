package com.wanda.member.basic.processor;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.basic.model.BasicPointTrans;

public class GoodsCalculateProcessor implements ItemProcessor<BasicPointTrans, BasicPointTrans>{
	private static final Log logger = LogFactory.getLog(GoodsCalculateProcessor.class);
	private String bizDate;
	private String transType;
	private String innerCode;
	@Override
	public BasicPointTrans process(BasicPointTrans item) throws Exception {
		item.setAdjResion("基础积分计算:卖品计算 订单ID:"+item.getOrderCode()+" 品项ID: "+item.getItemCode());
		item.setLevelPoint(item.getAmount().intValue());
		item.setTransType(this.getTransType());
		return item;
	}

	@AfterStep
	public void afterStep(StepExecution stepExecution){
		long endTime = new Date().getTime();
		long startTime = stepExecution.getStartTime().getTime();
		long runTime = endTime - startTime;
		logger.info("计算"+this.getTransType()+"交易记录，BIZ_DATE:"+this.getBizDate()+",影城内码："+this.getInnerCode()+",处理的记录数量:"+stepExecution.getWriteCount()+",用时:"+runTime+" ms");
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	
	
}
