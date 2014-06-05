package com.wanda.ccs.smsgateway.engine.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.smsgateway.console.GateWayApp;
import com.wanda.ccs.smsgateway.engine.AbstractSendMsgTask;
import com.wanda.ccs.smsgateway.engine.dao.CinemaDAO;
import com.wanda.ccs.smsgateway.engine.dao.TransCheckCodeDAO;
import com.wanda.ccs.smsgateway.exception.TaskCheckException;

/**
 * <b>Application name:</b>SMS GATEWAY<br>
 * <b>class desc:</b> {} <br>
 * <b>Date:</b>2013-11-04<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */
public class SendCheckCodeTask extends AbstractSendMsgTask {
	private static final Log logger = LogFactory
			.getLog(SendCheckCodeTask.class);

	public String getTaskName() {
		return this.getClass().getSimpleName();
	}

	public boolean taskRun() throws Exception {

		String innerCode = this.getNoPrefixMsg();
		
		int count = this.getCinemaDAOImpl().queryInnerCode(innerCode);
		
		if(count == 0){
			//影城内码在系统中不存在
			throw new TaskCheckException("影城内码:"+innerCode + "在系统中不存在。");
		}
		
		this.setSettleId(innerCode);
		//获取5分钟内的验证码
		Map<String, Object> checkCodeMap = this.getTransCheckCodeDAOImpl().queryTransCheckCode(this.getMobileNo());
		StringBuffer msg = new StringBuffer();

		String checkCode = null;
		if(null == checkCodeMap){
			String model = this.getConfigurationUtil().getString("send.model");
			if("0".equals(model)){
				// 生成六位验证码
				checkCode = RandomStringUtils.randomNumeric(6);
				// 写验证码表
				this.getTransCheckCodeDAOImpl().insertTransCheckCode(checkCode, this.getMobileNo());
				
				//下行验证码短信内容
				msg.append(this.getConfigurationUtil().getString("mt.checkcode.prefix"));
				msg.append(checkCode);
				msg.append(this.getConfigurationUtil().getString("mt.checkcode.suffix"));
			}else{
				//不重新生成验证码，直接发送错误短信信息。
				msg.append(this.getConfigurationUtil().getString("mt.checkcode.expired"));
			}
			
		}else{
			BigDecimal transCheckCodeId = (BigDecimal)checkCodeMap.get("TRANS_CHECK_CODE_ID");
			checkCode = String.valueOf(checkCodeMap.get("CHECK_CODE"));
			this.getTransCheckCodeDAOImpl().updateTransCheckCodeGenTime(transCheckCodeId);
			//下行验证码短信内容
			msg.append(this.getConfigurationUtil().getString("mt.checkcode.prefix"));
			msg.append(checkCode);
			msg.append(this.getConfigurationUtil().getString("mt.checkcode.suffix"));
		}

		// 写发送历史
		this.writeSendLog(msg.toString());
		// 向下行短信队列添加数据
		return this.sendSms(msg.toString());
	}

	public Log getLogger() {
		return logger;
	}

	public TransCheckCodeDAO getTransCheckCodeDAOImpl() {
		return GateWayApp.getBean("transCheckCodeDAO");
	}

	public CinemaDAO getCinemaDAOImpl() {
		return GateWayApp.getBean("cinemaDAO");
	}

}
