package com.wanda.mms.control.response;

import java.math.BigDecimal;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.data.TActResultMapper;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class TestDB {
	@Resource(name = "myBatisDAO")
	MyBatisDAO myBatisDAO = null;
	protected static ApplicationContext context= new FileSystemXmlApplicationContext("classpath*:spring/application*.xml");
	@Test
	public void test() {
		Assert.assertNotNull(myBatisDAO);
		MarketingEffectEvaluation marketingEffectEvaluation = context.getBean("marketingEffectEvaluation",MarketingEffectEvaluation.class);
		TActResultMapper mapper = myBatisDAO.sqlSession.getMapper(TActResultMapper.class);
		TActResult re = mapper.selectByPrimaryKey(new BigDecimal(21));
		Gson gson = new Gson();
		System.out.println(gson.toJson(re));
		FieldSet fieldSet = new FieldSet();
		Field field = new Field();
		field.destName="ACT_RESULT_ID";
		field.destValue=String.valueOf(re.getActResultId());
		fieldSet.put(field);
		
		field = new Field();
		field.destName="CMN_ACTIVITY_ID";
		field.destValue=String.valueOf(re.getCmnActivityId());
		fieldSet.put(field );
		//6 6 10 2 1
		field = new Field();
		field.destName="RES_SEGMENT_ID";
		field.destValue=String.valueOf(re.getResSegmentId());
		fieldSet.put(field );
		marketingEffectEvaluation.handle(fieldSet );
		System.out.println(gson.toJson(marketingEffectEvaluation.handleResult)); 
	}

}
