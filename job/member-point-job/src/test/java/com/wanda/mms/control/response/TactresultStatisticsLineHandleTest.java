package com.wanda.mms.control.response;

import java.math.BigDecimal;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.service.ActResultService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class TactresultStatisticsLineHandleTest {
	TactresultStatisticsLineHandle handle = null;
	@Resource(name = "actResultService")
	ActResultService actResultService = null;
	@Resource(name = "myBatisDAO")
	MyBatisDAO myBatisDAO = null;
	@Before
	public void setup(){
		 actResultService.getClass();
		 handle = new TactresultStatisticsLineHandle();
		 // update t_contact_history set has_response = 1  where act_target_id=497
	}
	
	@Test
	public void testHandle() {
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
		handle.handle(fieldSet);
		TActResult result = handle.reslut;
		Assert.assertNotNull(result);
		Assert.assertEquals(new BigDecimal(6), result.getAlterResCount());
		Assert.assertEquals(new BigDecimal(6), result.getResCount());
		Assert.assertEquals(new BigDecimal(10), result.getContactCount());
		Assert.assertEquals(new BigDecimal(2), result.getControlCount());
		Assert.assertEquals(new BigDecimal(1), result.getControlResCount());
	}

}
