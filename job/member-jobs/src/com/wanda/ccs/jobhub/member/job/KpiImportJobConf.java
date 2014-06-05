package com.wanda.ccs.jobhub.member.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class KpiImportJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/KpiImportJob")
		    	.withDescription("导入KPI指标；参数： fileId=T_FILE_ATTACH表中的主键。")
		        .withIdentity("KpiImportJob", jobGroup)
		        .build();
		}
		
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		String fileId = triggerDataMap.get("fileId");
		
	    // Trigger the job to run now
	    Trigger trigger = newTrigger()
	        .withIdentity("KpiImportJob-" + fileId, jobGroup)
	        .startNow() 
	        .build();
	    return trigger;
	}

	public boolean scheduleOnStart() {
		return false;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	public String getJobGroup() {
		return this.jobGroup;
	}

	public JobLoggingPhase[] getLoggingPhases() {
		return phases;
	}

}
