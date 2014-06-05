package com.wanda.ccs.smsgateway.console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.smsgateway.engine.MoEngine;

/**
 * 
 * <b>Application name:</b>SMS GateWay<br>
 * <b>class desc:</b> {} <br>
 * <b>Date:</b>2013-11-03<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */

public class GateWayManager {
	private static final Log logger = LogFactory.getLog(GateWayManager.class);
	
	private ExecutorService executor = Executors.newCachedThreadPool();	
	
	private MoEngine moEngine;
	
	public void startEngines() throws Exception{
		executor.submit(this.getMoEngine());
		logger.info("SMS GateWay服务启动完成!");
	}
	
	public void stopEngines(){
		this.getMoEngine().setStart(false);
		logger.info("SMS GateWay服务将在120秒内停止......");
		int count = 60;
		for (int i = 0; i < count; i++) {
			int activeCount = this.getMoEngine().getExecutor().getActiveCount();
			if(activeCount == 0){
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		logger.info("SMS GateWay服务停止完成!");
	}

	public MoEngine getMoEngine() {
		return moEngine;
	}

	public void setMoEngine(MoEngine moEngine) {
		this.moEngine = moEngine;
	}

}
