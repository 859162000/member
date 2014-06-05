package com.wanda.ccs.smsgateway.engine.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.smsgateway.engine.AbstractSendMsgTask;

public class SendErrorMsgTask extends AbstractSendMsgTask {
	private static final Log logger = LogFactory.getLog(SendErrorMsgTask.class);

	public String getTaskName() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected boolean taskRun() throws Exception {
		StringBuffer msg = new StringBuffer();
		msg.append("对不起，您发送的短信指令有误。请选择您需要的短信服务，重新发送正确的短信指令。");
		
		//写发送历史
		this.writeSendLog(msg.toString());
		//向下行短信队列添加数据
		return this.sendSms(msg.toString());
	}

	@Override
	protected Log getLogger() {
		return logger;
	}
	
	
}
