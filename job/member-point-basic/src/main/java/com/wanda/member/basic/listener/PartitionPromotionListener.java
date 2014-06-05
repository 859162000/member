package com.wanda.member.basic.listener;

import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.InitializingBean;

public class PartitionPromotionListener  extends StepExecutionListenerSupport implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
