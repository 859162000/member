package com.wanda.member.countermgt.handle;

import org.springframework.context.ApplicationContext;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;
import com.wanda.member.countermgt.service.CounterComputeFacade;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.service.MbrApplicationContext;

public class CounterComputeHandle implements LineHandle {
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	CounterComputeFacade facade = null;
	static {
		// EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		myBatisDAO = context.getBean("myBatisDAO", MyBatisDAO.class);
	}
	public CounterComputeHandle(){
		facade = context.getBean("counterComputeFacade", CounterComputeFacade.class);
	}
	@Override
	public int handle(FieldSet fieldSet) {
		Field filed = fieldSet.getFieldByName("MEMBER_ID");
		LogUtils.info("filed:"+filed.destValue);
		facade.computeByMember(filed.destValue);
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
