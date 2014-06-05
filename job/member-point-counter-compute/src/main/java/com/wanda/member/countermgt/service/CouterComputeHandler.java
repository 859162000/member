package com.wanda.member.countermgt.service;

import org.springframework.context.ApplicationContext;

import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.service.MbrApplicationContext;

public class CouterComputeHandler implements LineHandle {
	static ApplicationContext context = null;
	
	static{
//		EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
//		mbr = myBatisDAO.getConnection();
	}
	CounterComputeFacade facade = null;
	public CouterComputeHandler(){
		facade = context.getBean("counterComputeFacade", CounterComputeFacade.class);
	}
	@Override
	public int handle(FieldSet fieldSet) {
		
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
