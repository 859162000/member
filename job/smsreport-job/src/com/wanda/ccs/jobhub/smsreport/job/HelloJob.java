package com.wanda.ccs.jobhub.smsreport.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.service.SendLineMemberSmsService;

//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class HelloJob implements Job {

	@InstanceIn(path="ChangeMemberPointService")
	private SendLineMemberSmsService sendLineMemberSmsService;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
//		try {
//			sendLineMemberSmsService.sendLineSms("2013-08-17");
//		} catch (SystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new JobExecutionException(e.getMessage());
//		}
		
//		int count = 5;
//		for(int i=0 ; i < count ; i++) {
//			System.out.println("I am running! " + i + " of " + count + " Trigger=" + context.getTrigger().getKey());
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println("I am finished! Trigger=" + context.getTrigger().getKey());
		//if(System.currentTimeMillis() % 3 == 1) {
		//	throw new JobExecutionException("Refired by self!", true);
		//}
		//context.setResult("count=1111");
	}

}
