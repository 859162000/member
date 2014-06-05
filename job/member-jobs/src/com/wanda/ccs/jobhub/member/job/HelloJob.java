package com.wanda.ccs.jobhub.member.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class HelloJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		int count = 5;
		for(int i=0 ; i < count ; i++) {
			System.out.println("I am running! " + i + " of " + count + " Trigger=" + context.getTrigger().getKey());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("I am finished! Trigger=" + context.getTrigger().getKey());
		//if(System.currentTimeMillis() % 3 == 1) {
		//	throw new JobExecutionException("Refired by self!", true);
		//}
		//context.setResult("count=1111");
	}

}
