package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.StatusChangingService;

public class StatusChangingJob implements Job {
	
	@InstanceIn(path = "StatusChangingService")
	private StatusChangingService service;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			service.updateStatus();
		}
		catch(Throwable t) {
			throw new JobExecutionException(t);
		}
	}


}
