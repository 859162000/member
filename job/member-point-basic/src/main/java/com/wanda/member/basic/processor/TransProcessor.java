package com.wanda.member.basic.processor;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.basic.model.BasicPointTrans;

public class TransProcessor implements ItemProcessor<BasicPointTrans, BasicPointTrans>{
	private static final Log logger = LogFactory.getLog(TransProcessor.class);
	private String bizDate;
	private String innerCode;
	private String transType;
	@Override
	public BasicPointTrans process(BasicPointTrans trans) throws Exception {
		//价格四舍五入取整
		trans.setAmount(trans.getAmount().setScale(0, BigDecimal.ROUND_HALF_UP));
		//过滤价格小于0的数据
		if(trans.getAmount().intValue() <= 0){
			return null;
		}
		trans.setTransType(this.getTransType());
		return trans;
	}
	
	@AfterStep
	public void afterStep(StepExecution stepExecution){
		long endTime = new Date().getTime();
		long startTime = stepExecution.getStartTime().getTime();
		long runTime = endTime - startTime;
		logger.info("载入"+this.getTransType()+"交易记录，BIZ_DATE:"+this.getBizDate()+",影城内码："+this.getInnerCode()+",需要处理的记录数量:"+stepExecution.getWriteCount()+",用时:"+runTime+" ms");
	}
	
	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	
}
