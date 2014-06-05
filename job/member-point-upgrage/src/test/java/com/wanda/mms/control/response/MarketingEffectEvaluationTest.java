package com.wanda.mms.control.response;

import java.math.BigDecimal;

import javax.activation.FileDataSource;
import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class MarketingEffectEvaluationTest {
	MarketingEffectEvaluation marketingEffectEvaluation = context.getBean("marketingEffectEvaluation",MarketingEffectEvaluation.class);
	MyBatisDAO myBatisDAO = context.getBean("myBatisDAO",MyBatisDAO.class);
	protected static ApplicationContext context= new FileSystemXmlApplicationContext("classpath*:spring/application*.xml");
	@Test
	public void testHandle(){
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		FieldSet fieldSet = new FieldSet();
		Field field = new Field();
		field.destName="ACT_RESULT_ID";
		field.destValue="101";
		fieldSet.put(field);
		
		field = new Field();
		field.destName="CMN_ACTIVITY_ID";
		field.destValue="682";
		fieldSet.put(field );
		//6 6 10 2 1
		field = new Field();
		field.destName="RES_SEGMENT_ID";
		field.destValue="694";
		fieldSet.put(field );
		marketingEffectEvaluation.handle(fieldSet);
		System.out.println(marketingEffectEvaluation.handleResult);
		TActResult result = marketingEffectEvaluation.handleResult;
		Assert.assertNotNull(result);
		Assert.assertEquals(new BigDecimal(6), result.getAlterResCount());
		Assert.assertEquals(new BigDecimal(6), result.getResCount());
		Assert.assertEquals(new BigDecimal(10), result.getContactCount());
		Assert.assertEquals(new BigDecimal(2), result.getControlCount());
		Assert.assertEquals(new BigDecimal(1), result.getControlResCount());
		
//		mapper.getActResult("101");
	}
	
	@Test
	public void testGetActResult(){
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		FieldSet fieldSet = new FieldSet();
		Field field = new Field();
		field.destName="ACT_RESULT_ID";
		field.destValue="101";
		fieldSet.put(field );
		
		field = new Field();
		field.destName="CMN_ACTIVITY_ID";
		field.destValue="682";
		fieldSet.put(field );
		//6 6 10 2 1
		field = new Field();
		field.destName="RES_SEGMENT_ID";
		field.destValue="694";
		fieldSet.put(field );
		TActResult result = marketingEffectEvaluation.getActResult(fieldSet , mapper);
		System.out.println(result.getAlterResCount());
		Assert.assertNotNull(result);
		Assert.assertEquals(new BigDecimal(6), result.getAlterResCount());
		Assert.assertEquals(new BigDecimal(6), result.getResCount());
		Assert.assertEquals(new BigDecimal(10), result.getContactCount());
		Assert.assertEquals(new BigDecimal(2), result.getControlCount());
		Assert.assertEquals(new BigDecimal(1), result.getControlResCount());
	}
	
	@Test
	public void testGetAlterResponseCount() {
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		BigDecimal cnt = mapper.getAlterResponseCount(new java.math.BigDecimal("101"));
		BigDecimal recnt = new java.math.BigDecimal(6);
		Assert.assertTrue(cnt.equals(recnt));
	}
	@Test
	public void testGetControlcount(){
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		BigDecimal cnt = mapper.getControlcount(new BigDecimal("682"));
		Assert.assertTrue(cnt.intValue()>0);
	}
	@Test
	public void testGetRelResponseCount() {
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		BigDecimal cnt = mapper.getRelResponseCount(new java.math.BigDecimal("101"));
		BigDecimal recnt = new java.math.BigDecimal(6);
		Assert.assertTrue(cnt.equals(recnt));
	}
	@Test
	public void testupdateResult() {
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		TActResult result =new TActResult();
		result.setActResultId(new BigDecimal("101"));
		result.setResCount(new BigDecimal("9"));
		int cnt = mapper.updateResult(result);
		System.out.println(cnt);
		
	}
	@Test
	public void testGetContractResCount() {
		MarketingEffectMapper mapper = myBatisDAO.sqlSession.getMapper(MarketingEffectMapper.class);
		TActResult result =new TActResult();
		result.setActResultId(new BigDecimal("101"));
		result.setResCount(new BigDecimal("9"));
		BigDecimal cnt = mapper.getControlResCount(new BigDecimal("682"), new BigDecimal("694"));
		System.out.println(cnt);
		
	}
}
