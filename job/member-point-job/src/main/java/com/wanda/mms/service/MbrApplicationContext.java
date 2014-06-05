package com.wanda.mms.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public final class MbrApplicationContext {
	protected static ApplicationContext context= new FileSystemXmlApplicationContext("classpath*:spring/application*.xml");
	
	public static ApplicationContext getInstance() {
		return context;
	}
	
}
