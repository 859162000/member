package com.wanda.member.basic.writer;

import java.util.ArrayList;
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

public class PointChangeWriter implements ItemStreamWriter<BasicPointTrans>,
		InitializingBean {
	private static final Log logger = LogFactory
			.getLog(PointChangeWriter.class);
	private String innerCode;
	private ItemWriter<BasicPointTrans> writer;

	@Override
	public void write(List<? extends BasicPointTrans> items) throws Exception {
		Map<String, List<BasicPointTrans>> fourTicketMap = new HashMap<String, List<BasicPointTrans>>();
		for (BasicPointTrans basicPointTrans : items) {
			String key = basicPointTrans.getInnerCode() + "_"
					+ basicPointTrans.getMemberId();
			if (fourTicketMap.containsKey(key)) {
				fourTicketMap.get(key).add(basicPointTrans);
			} else {
				List<BasicPointTrans> transList = new ArrayList<BasicPointTrans>();
				transList.add(basicPointTrans);
				fourTicketMap.put(key, transList);
			}
		}
		// 过滤
		this.filterTicketMap(fourTicketMap);

		// 处理变更后积分
		for (Iterator<String> iterator = fourTicketMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			List<BasicPointTrans> list = fourTicketMap.get(key);
			int orgPoint = 0;// 变更前积分
			int pointBlance = 0;// 变更后积分
			for (int i = 0; i < list.size(); i++) {
				BasicPointTrans trans = list.get(i);
				if (i == 0) {
					orgPoint = trans.getOrgPoint();
				} else {
					orgPoint = pointBlance;
				}
				int levelPoint = trans.getLevelPoint();// 该条记录积分值
				pointBlance = orgPoint + levelPoint;
				trans.setOrgPoint(orgPoint);
				trans.setPointBlance(pointBlance);
			}
			writer.write(list);
		}
	}

	private void filterTicketMap(
			Map<String, List<BasicPointTrans>> fourTicketMap) {
		Iterator<Map.Entry<String, List<BasicPointTrans>>> it = fourTicketMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<BasicPointTrans>> entry = it.next();
			if (entry.getValue().size() == 1) {
				// 只有一张影票 不需要更新
				it.remove();
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
