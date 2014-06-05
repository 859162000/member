package com.wanda.member.basic.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.util.CollectionUtils;

import com.wanda.member.basic.model.BasicPointTrans;

public class TicketCalculateProcessor implements ItemProcessor<BasicPointTrans, BasicPointTrans>{
	private static final Log logger = LogFactory.getLog(TicketCalculateProcessor.class);
	private ResourceAwareItemReaderItemStream<BasicPointTrans> fourTicketReader;
	private String bizDate;
	private String transType;
	private String innerCode;
	private Map<String, List<BasicPointTrans>> fourTicketMap;
	@Override
	public BasicPointTrans process(BasicPointTrans item) throws Exception {
		
		//过滤四张票
		if(this.checkFourTicket(item)){
			return null;
		}
		
		item.setAdjResion("基础积分计算:影票计算 订单ID:"+item.getOrderCode()+" 票号: "+item.getItemCode());
		item.setLevelPoint(item.getAmount().intValue());
		item.setTransType(this.getTransType());
		return item;
	}
	
	private boolean checkFourTicket(BasicPointTrans item){
		boolean flag = true;
		String key = item.getInnerCode() + "_" + item.getMemberId();
		//判断会员是否在MAP中
		if (this.fourTicketMap.containsKey(key)) {
			//取出四张票，判断当前处理的记录是否在四张票内
			List<BasicPointTrans> fourList = this.fourTicketMap.get(key);
			for (BasicPointTrans fourTrans : fourList) {
				if(fourTrans.getItemCode().equals(item.getItemCode())){
					flag = false;
					break;
				}
			}
		}else{
			flag = false;
		}
		return flag;
	}
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution){
		List<BasicPointTrans> fourTicketList = new ArrayList<BasicPointTrans>();
		fourTicketMap = new HashMap<String, List<BasicPointTrans>>();
		try {
			fourTicketReader.open(new ExecutionContext());
			BasicPointTrans item = new BasicPointTrans();
			while (true) {
				item = fourTicketReader.read();
				if(item != null){
					fourTicketList.add(item);
				}else{
					break;
				}
			}
			fourTicketReader.close();
		} catch (UnexpectedInputException e) {
			logger.error("Unexpected Input For Ticket Rule XML.", e);
		} catch (ParseException e) {
			logger.error("Parse Error For Ticket Rule XML.", e);
		} catch (NonTransientResourceException e) {
			logger.error("Non Transient Resource For Ticket Rule XML.", e);
		} catch (Exception e) {
			logger.error("Read Ticket Rule XML Error.", e);
		}
		
		if(!CollectionUtils.isEmpty(fourTicketList)){
			for (BasicPointTrans basicPointTrans : fourTicketList) {
				String key = basicPointTrans.getInnerCode() + "_" + basicPointTrans.getMemberId();
				if(fourTicketMap.containsKey(key)){
					fourTicketMap.get(key).add(basicPointTrans);
				}else{
					List<BasicPointTrans> list = new ArrayList<BasicPointTrans>();
					list.add(basicPointTrans);
					fourTicketMap.put(key, list);
				}
			}
		}
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

	public ResourceAwareItemReaderItemStream<BasicPointTrans> getFourTicketReader() {
		return fourTicketReader;
	}

	public void setFourTicketReader(
			ResourceAwareItemReaderItemStream<BasicPointTrans> fourTicketReader) {
		this.fourTicketReader = fourTicketReader;
	}
	
}
