package com.wanda.member.countermgt.handle;

import java.math.BigDecimal;

import org.springframework.context.ApplicationContext;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;
import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.service.CounterComputeFacade;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.service.MbrApplicationContext;

public class CounterComputeRule1Handle implements LineHandle {
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	CounterComputeFacade facade = null;
	static {
		// EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		myBatisDAO = context.getBean("myBatisDAO", MyBatisDAO.class);
	}
	public CounterComputeRule1Handle(){
		facade = context.getBean("counterComputeFacade", CounterComputeFacade.class);
	}
	@Override
	public int handle(FieldSet fieldSet) {
		Field filed = fieldSet.getFieldByName("MEMBER_ID");
		Field cinamefiled = fieldSet.getFieldByName("REGIST_CINEMA_ID");
		LogUtils.info("filed:"+filed.destValue);
		LogUtils.info("cinamefiled:"+cinamefiled.destValue);
		TMember memeber = new TMember();
		memeber.setMemberId(new BigDecimal(filed.destValue));
		memeber.setMemberNo(filed.destValue);
		memeber.setRegistCinemaId(new BigDecimal(cinamefiled.destValue));
		try {
			facade.setCounter(filed.destValue, cinamefiled.destValue );
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		facade.computeByMember(filed.destValue);
		return 1;
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
