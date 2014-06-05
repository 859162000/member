package com.wanda.member.validate;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-Mbr-Point.xml")
public class ValidatePointHisTest {
	@Resource
	ValidatePointHis hisva= null;
//	@Test
	public void testValidate() {
//		hisva.validate("5951636");
		EtlBean prmapping = EtlConfig.getInstance().getEtlBean("validatepoint");
		SolarEtlExecutor.runetlFixedThread(prmapping,null, 4);
	}

}
