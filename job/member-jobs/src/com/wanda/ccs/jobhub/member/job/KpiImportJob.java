package com.wanda.ccs.jobhub.member.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.KpiService;

public class KpiImportJob implements Job{
private Logger logHelper = Logger.getLogger("Integration-HANDOFF");
	
	@InstanceIn(path="KpiService")
	private KpiService kpiService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
	    Long fileId = Long.parseLong(jobDataMap.get("fileId").toString());
	    kpiService.execute(fileId);
	}

}
