package com.wanda.member.validate;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.mms.service.MbrApplicationContext;

public class ValidatePointFacade  implements LineHandle{
	static ApplicationContext context = null;
	ValidatePointHis val = null;
	static {
		// EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
	}
	public ValidatePointFacade(){
		val = context.getBean(ValidatePointHis.class);
	}
	
	@Override
	public int handle(FieldSet fieldSet) {
		Field filed = fieldSet.getFieldByName("MEMBER_ID");
		String memberId = filed.destValue;
		return val.validate(memberId);
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
