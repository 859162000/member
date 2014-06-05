package com.wanda.member.basic.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.wanda.member.basic.model.BasicPointTrans;

public class FourTicketWriter implements ItemStreamWriter<BasicPointTrans>, InitializingBean {
	private static final Log logger = LogFactory.getLog(FourTicketWriter.class);
	private String innerCode;
	private ItemWriter<BasicPointTrans> writer;
	@Override
	public void write(List<? extends BasicPointTrans> items) throws Exception {
		Map<String, List<BasicPointTrans>> fourTicketMap = new HashMap<String, List<BasicPointTrans>>();
		for (BasicPointTrans basicPointTrans : items) {
			// 影票信息放入MAP中，计算四张票
			String key = basicPointTrans.getInnerCode() + "_" + basicPointTrans.getMemberId();
			if (fourTicketMap.containsKey(key)) {
				fourTicketMap.get(key).add(basicPointTrans);
			} else {
				List<BasicPointTrans> transList = new ArrayList<BasicPointTrans>();
				transList.add(basicPointTrans);
				fourTicketMap.put(key, transList);
			}
		}
		//过滤会员，保留影票大于4张的会员
		this.filterMember(fourTicketMap);
		//过滤票金额，保留4张有效票
		this.filterTicketMap(fourTicketMap);
		
		Iterator<Map.Entry<String, List<BasicPointTrans>>> it = fourTicketMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<BasicPointTrans>> entry=it.next(); 
			writer.write(entry.getValue());
		}
		logger.info("处理前四张影票, 影城内码:"+this.getInnerCode()+",需要处理四张票的会员数:"+fourTicketMap.size());
	}
	
	private void filterTicketMap(Map<String, List<BasicPointTrans>> fourTicketMap) {
		for (Iterator<String> iterator = fourTicketMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			List<BasicPointTrans> transList = fourTicketMap.get(key);

			Collections.sort(transList, new Comparator<BasicPointTrans>() {

				@Override
				public int compare(BasicPointTrans arg0, BasicPointTrans arg1) {
					return arg1.getAmount().compareTo(arg0.getAmount());
				}

			});
			// 过滤四张票
			int ticketCount = 0;
			Iterator<BasicPointTrans> transIt = transList.iterator();
			while(transIt.hasNext()){
				transIt.next();
				ticketCount++;
			    if (ticketCount > 4) {
			    		transIt.remove();
				}
			} 

		}
	}
	
	private void filterMember(Map<String, List<BasicPointTrans>> fourTicketMap){
		
		Iterator<Map.Entry<String, List<BasicPointTrans>>> it = fourTicketMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<BasicPointTrans>> entry=it.next(); 
			if(entry.getValue().size() < 4){
				//从MAP中删除影票小于4张的会员
				it.remove();
			}
		}
        
		for (Iterator<String> iterator = fourTicketMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			if(fourTicketMap.get(key).size() < 4){
				//从MAP中删除影票小于4张的会员
				fourTicketMap.remove(key);
			}
		}
	}
	@Override
	public void open(ExecutionContext executionContext)
			throws ItemStreamException {
		if (writer instanceof ItemStream) {
			((ItemStream) writer).open(executionContext);
		}
	}

	@Override
	public void update(ExecutionContext executionContext)
			throws ItemStreamException {
		if (writer instanceof ItemStream) {
			((ItemStream) writer).update(executionContext);
		}
	}

	@Override
	public void close() throws ItemStreamException {
		if (writer instanceof ItemStream) {
			((ItemStream) writer).close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(writer, "The 'writer' may not be null");
	}
	public ItemWriter<BasicPointTrans> getWriter() {
		return writer;
	}

	public void setWriter(ItemWriter<BasicPointTrans> writer) {
		this.writer = writer;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

}
