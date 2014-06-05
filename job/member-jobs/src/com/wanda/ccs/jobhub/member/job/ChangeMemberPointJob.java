package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.ChangeMemberPointService;

public class ChangeMemberPointJob implements Job {

	@InstanceIn(path="ChangeMemberPointService")
	private ChangeMemberPointService changeMemberPointService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
	    Long fileId = Long.parseLong(jobDataMap.get("fileId").toString());
	    changeMemberPointService.execute(fileId);
	}

}
