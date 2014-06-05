package com.wanda.mms.control.stream;

import org.springframework.context.ApplicationContext;

import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;
import com.wanda.member.upgrade.service.MemberUpgradeHandler;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.service.MbrApplicationContext;




public class UpLevelLineHandle implements LineHandle{
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	
	static{
//		EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		myBatisDAO = context.getBean("myBatisDAO",MyBatisDAO.class);
//		mbr = myBatisDAO.getConnection();
	}
	
	private MemberUpgradeHandler handler = null;
	public UpLevelLineHandle(){
		handler = context.getBean(MemberUpgradeHandler.class);
	}
	@Override
	public int handle(FieldSet fieldset) {
		return handler.handle(fieldset);
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
