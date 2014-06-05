package com.wanda.ccs.smsgateway.engine;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.ReadFromQueue;
import com.wanda.ccs.smsgateway.console.GateWayApp;
import com.wanda.ccs.smsgateway.constant.DelimiterConst;
import com.wanda.ccs.smsgateway.constant.MoMsgConst;
import com.wanda.ccs.smsgateway.constant.SmsCommandConst;
import com.wanda.ccs.smsgateway.engine.dao.EngineLogDAO;
import com.wanda.ccs.smsgateway.engine.model.EngineLog;
/**
 * 
 * <b>Application name:</b>SMS GateWay<br>
 * <b>class desc:</b> {上行短信处理引擎} <br>
 * <b>Date:</b>2013-11-03<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */
public class MoEngine extends AbstractEngine{
	private static final Log logger = LogFactory.getLog(MoEngine.class);
	private String queueName = "001_reply";
	private ThreadPoolTaskExecutor executor;
	private Configuration configurationUtil;
	
	public void run() {
		logger.info("[日志] 开始处理上行短信消息队列！");
		ServiceUrl.setMqUrl(configurationUtil.getString("mq.serverip"));
 	    ServiceUrl.setWebServiceUrl(configurationUtil.getString("mq.webserviceurl"));
 	    queueName = configurationUtil.getString("mq.queuename");
		int messageCount=0;
		ReadFromQueue rfq = new ReadFromQueue();
		String message = "";
		Long logId = 0L;
		String taskName = "";
		String moPrefix = configurationUtil.getString("mo.prefix");
		moPrefix = StringUtils.upperCase(moPrefix);
		while (isStart) {
			try {
				//Thread.sleep(5000);
				message = rfq.deliveryMessage(queueName,messageCount);
				//phone|||msgContent|||linkid|||platform|||serviceUp|||spNumber
				//message = "13904080136|||Mbyz112|||linkid|||platform|||ServiceUp|||SpNumber";
			    if (logger.isDebugEnabled()){
			    	logger.debug("[日志] 读取上行短信消息队列内容： '" + message + "'");
			    }
			    //记录Engine日志
			    logId = this.writeEngineLog(message);
			    
			    //根据接口协议格式，拆分短信字符串
				String[] moArr = StringUtils.split(message, DelimiterConst.MO_SMS_DELIMITER);
				Assert.isTrue(moArr.length > MoMsgConst.MSG_CONTENT_INDEX, "解析上行队列内容错误，上行队列内容为："+message);
				//上行短信用户发送的内容
				String moMsgContent = moArr[MoMsgConst.MSG_CONTENT_INDEX];
				moMsgContent = StringUtils.upperCase(moMsgContent);
				   
				if(!StringUtils.isEmpty(moMsgContent) && moMsgContent.startsWith(moPrefix)){
					//获取去掉MA之后的短信内容
					String noMaPrefixMsg = moMsgContent.substring(moPrefix.length(), moMsgContent.length());
					//获取短信指令处理任务
					ITask task = this.taskHandler(noMaPrefixMsg);
					String noPrefixMsg = noMaPrefixMsg.substring(SmsCommandConst.CHECK_CODE_COMD.length(), noMaPrefixMsg.length());
					if(null != task && !StringUtils.isEmpty(noPrefixMsg)){
						task.setLogId(logId);
						task.setEngineName(MoEngine.class.getSimpleName());
						task.setOriginMsg(message);
						task.setNoPrefixMsg(noPrefixMsg);
						//加入线程池
						executor.execute(task);
						
					}
				}
				
			} catch (Exception e) {
				System.err.println("[错误] 处理上行短信消息队列出现异常！");
				logger.error("[错误] 处理上行短信消息队列出现异常！", e);
				try {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					if(logId == 0){
						//记录Engine日志
					    EngineLog engineLog = new EngineLog();
					    engineLog.setEngineName(MoEngine.class.getSimpleName());
					    engineLog.setTaskName(taskName);
					    engineLog.setDeliveryMsg(message);
					    EngineLogDAO engineLogDAOImpl = GateWayApp.getBean("engineLogDAO");
					    engineLogDAOImpl.insertEngineLog(engineLog);
					    logId = engineLog.getLogId();
					}
					EngineLogDAO engineLogDAOImpl = GateWayApp.getBean("engineLogDAO");
				    engineLogDAOImpl.insertEngineError(logId, sw.toString());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}//end_try
		}
		
		Thread.currentThread().interrupt();
		logger.info("停止处理上行短信消息队列！");
	}
	
	/**
	 * @param message
	 * @return
	 * @TODO 写EngineLog表时，可能会影响处理消息队列的性能，如果出现消息队列处理的性能问题需要优化这里
	 */
	private Long writeEngineLog(String message){
	    EngineLog engineLog = new EngineLog();
	    engineLog.setEngineName(MoEngine.class.getSimpleName());
	    engineLog.setDeliveryMsg(message);
	    EngineLogDAO engineLogDAOImpl = GateWayApp.getBean("engineLogDAO");
	    engineLogDAOImpl.insertEngineLog(engineLog);
	    return engineLog.getLogId();
	}
	private ITask taskHandler(String noMaPrefixMsg){
		ITask task = null;
		if(StringUtils.startsWithIgnoreCase(noMaPrefixMsg, SmsCommandConst.CHECK_CODE_COMD)){
			task = GateWayApp.getBean("sendCheckCodeTask");
		}
		
		return task;
	}
	
	private void sendErrorMsg(String message, Long logId){
		ITask task = GateWayApp.getBean("sendErrorMsgTask");
		task.setLogId(logId);
		task.setEngineName(MoEngine.class.getSimpleName());
		task.setOriginMsg(message);
		executor.execute(task);
	}
	public Configuration getConfigurationUtil() {
		return configurationUtil;
	}

	public void setConfigurationUtil(Configuration configurationUtil) {
		this.configurationUtil = configurationUtil;
	}

	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}

}
