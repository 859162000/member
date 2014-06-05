package com.wanda.mms.control.response;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.service.MbrApplicationContext;


/**
 *	会员营销统计 统计响应 
 * @author wangshuai
 * @date 2013-09-21	
 */


public class TactresultStatisticsLineHandle implements LineHandle{
	private ApplicationContext context = MbrApplicationContext.getInstance();
	public TActResult reslut;
	@Override
	public int handle(FieldSet fieldSet) {
//		System.out.println(this.threadGroup);
		MarketingEffectEvaluation marketingEffectEvaluation = context.getBean(MarketingEffectEvaluation.class);
		marketingEffectEvaluation.handle(fieldSet);
		reslut = marketingEffectEvaluation.handleResult;
		Gson gson = new Gson();		
		System.out.println(gson.toJson(reslut));
		return 0;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	
}
