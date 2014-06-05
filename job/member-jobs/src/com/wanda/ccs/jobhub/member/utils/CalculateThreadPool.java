package com.wanda.ccs.jobhub.member.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wanda.ccs.jobhub.member.job.CampaignCalculateJob.CalLog;
import com.wanda.ccs.jobhub.member.job.CampaignCalculateJob.SubTask;

/**
 * 计算线程池
 * 
 * @author ZR
 *
 */
public class CalculateThreadPool {
	
		public static final int NUMBER = 10;
		private ExecutorService executorService = null;
	    private CompletionService<CalLog> completionService = null;
	    private static CalculateThreadPool POOL = null;
	    
	    private CalculateThreadPool() {}
	    
	    public static CalculateThreadPool getInstance() {
	    	if(POOL == null) {
	    		POOL = new CalculateThreadPool();
	    	}
	    	return POOL;
	    }
	    
	    public Future<CalLog> submitTest(Callable<CalLog> task) {
	    	if(executorService == null || completionService == null) {
	    		init();
	    	}
	    	
	    	return completionService.submit(task);
	    }
	    
	    public Future<CalLog> submit(SubTask task) {
	    	if(executorService == null || completionService == null) {
	    		init();
	    	}
	    	
	    	return completionService.submit(task);
	    }
	    
	    public void init() {
	    	System.out.println("thread pool init");
	    	this.executorService = Executors.newFixedThreadPool(NUMBER);
	    	this.completionService = new ExecutorCompletionService<CalLog>(executorService);
	    }
	    
	    public void destory() {
	    	try {
	    		if(executorService != null) {
	    			this.executorService.shutdown();
					if(!this.executorService.awaitTermination(30, TimeUnit.SECONDS)) {
						this.executorService.shutdownNow();
					}
	    		}
	    		System.out.println("thread pool stop");
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.executorService.shutdownNow();
				System.out.println("thread pool stop fail, force stop!");
			} finally {
				this.executorService = null;
		    	this.completionService = null;
			}
	    }
	    
	}