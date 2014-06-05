package com.wanda.ccs.jobhub.smsreport.job;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.DateBuilder.IntervalUnit;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class HelloJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
		    // define the job and tie it to our HelloJob class
			job = newJob(PathletVirtualJob.class)
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/smsreport/HelloJob")
		        .withIdentity("hello-smsreport", jobGroup)
		        .build();
		}
		return job;
	}

	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
	    // Trigger the job to run now, and then repeat every 15 seconds
	    Trigger trigger = newTrigger()
	        .withIdentity("hello-", jobGroup)
	        .startNow()
	        .withSchedule(simpleSchedule()
	                .withIntervalInSeconds(15)
	                .repeatForever())     
	        .build();
		
	    //Trigger trigger = newTrigger()
		//        .withIdentity("HelloJob" + System.currentTimeMillis(), jobGroup)
		//        .startAt(futureDate(30, IntervalUnit.SECOND)) 
		//        .build();
	    
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
