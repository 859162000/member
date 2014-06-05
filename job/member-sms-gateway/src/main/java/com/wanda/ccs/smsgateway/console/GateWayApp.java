package com.wanda.ccs.smsgateway.console;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wanda.ccs.smsgateway.engine.MoEngine;
import com.wanda.ccs.smsgateway.utils.OSUtils;

/**
 * 
 * <b>Application name:</b>SMS GateWay<br>
 * <b>class desc:</b> {} <br>
 * <b>Date:</b>2013-11-03<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */
public final class GateWayApp {

	private static final Log logger = LogFactory.getLog(GateWayApp.class);

	private static GateWayApp instance = null;

	private GateWayApp() {
	}

	/**
	 * 
	 * @function <h>{单例模式 工厂方法}</h>
	 * @return
	 */
	synchronized public static GateWayApp getInstance() {
		if (instance == null) {
			instance = new GateWayApp();
		}
		return instance;
	}

	private static ConfigurableApplicationContext context;

	/**
	 * 
	 * @function <h>{判断当前 引擎是否启动}</h>
	 * @return T/F 启动/未启动
	 */
	public boolean isStarted() {
		return context == null ? false : true;
	}

	/**
	 * 
	 * @function <h>{获取配置的静态信息}</h>
	 * @return
	 */
	public String getClientInfo() {
		Configuration cfg = context.getBean("configurationUtil",
				Configuration.class);
		StringBuffer sb = new StringBuffer();
		sb.append("# mq.serverip :").append(cfg.getString("mq.serverip"))
				.append(OSUtils.getEnterKey());
		sb.append(OSUtils.getEnterKey());
		return sb.toString();
	}

	/**
	 * 
	 * @function <h>{获取运行时的一些引擎信息}</h>
	 * @return
	 */
	public String getRunInfo() {
		StringBuffer sb = new StringBuffer();
		MoEngine moEngine = (MoEngine)context.getBean("moEngine");
		sb.append("# MoEngine Active Count :").append(moEngine.getExecutor().getActiveCount()).append(OSUtils.getEnterKey());
		sb.append("# MoEngine Queue Count :").append(moEngine.getExecutor().getThreadPoolExecutor().getQueue().size()).append(OSUtils.getEnterKey());
		return sb.toString();
	}

	/**
	 * 
	 * @function <h>{停止Spring IOC容器}</h>
	 * @throws Exception
	 */
	public void stopApp() throws Exception {
		// TODO NEED TEST
		if (isStarted()) {
			GateWayManager mng = (GateWayManager) context.getBean("gateWayManager");
			mng.stopEngines();
			context.close();
			context = null;
		}
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @return
	 */
	public void startApp() throws Exception {
		if (isStarted()) {
			if (logger.isInfoEnabled())
				logger.info("错误：误操作，用户重复启动SMS GateWay服务 ......");
		} else {
			try {
				if (logger.isInfoEnabled())
					logger.info("日志：开始启动SMS GateWay服务......");
				context = new ClassPathXmlApplicationContext(
						"classpath:conf/spring/application-context.xml");
				context.registerShutdownHook();
				
				GateWayManager mng = (GateWayManager) context.getBean("gateWayManager");

				// 启动服务
				mng.startEngines();
				
				if (logger.isInfoEnabled())
					logger.info("日志：启动SMS GateWay服务 成功......");
			} catch (Exception e) {
				logger.error("日志：启动SMS GateWay服务启动发生异常 ......", e);
				throw e;
			} // end_try
		}
	} // end_fun
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		return (T) context.getBeansOfType(clazz);
	}
	
	public static Configuration getConfUtil(){
		return context.getBean("configurationUtil", Configuration.class);
	}

} // end_clazz
