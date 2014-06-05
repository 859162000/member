package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.CreateCardService;

/**
 * 新建卡任务
 * @author YangJianbin
 *
 */
public class CreateCardJob implements Job {
	
	@InstanceIn(path="CreateCardService")
	private CreateCardService createCardService;;
	
	public void execute(JobExecutionContext context) {
	    createCardService.updateCreateCard();
	}



}
