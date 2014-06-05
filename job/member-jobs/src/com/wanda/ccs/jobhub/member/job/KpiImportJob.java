package com.wanda.ccs.jobhub.member.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.KpiService;

public class KpiImportJob implements Job{
private Logger logHelper = Logger.getLogger("Integration-HANDOFF");
	
	@InstanceIn(path="KpiService")
	private KpiService kpiService;


	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			Long fileId = Long.parseLong(jobDataMap.get("fileId").toString());
			kpiService.execute(fileId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
	}

}
