package com.wanda.ccs.jobhub.member.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class StatusChangingJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = {JobLoggingPhase.FAILED, JobLoggingPhase.VETOED};
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/StatusChangingJob")
		        .withIdentity("StatusChangingJob", jobGroup)
		        .build();
		}
		return job;
	}

	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
	    // Trigger the job to run now, and then repeat every 10 seconds
	    Trigger trigger = newTrigger()
	        .withIdentity("StatusChangingJob-", jobGroup)
	        .startNow()
	        .withSchedule(simpleSchedule()
	                .withIntervalInSeconds(10)
	                .repeatForever())     
	        .build();
	    
	    return trigger;
	}

	public boolean scheduleOnStart() {
		return true;
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
