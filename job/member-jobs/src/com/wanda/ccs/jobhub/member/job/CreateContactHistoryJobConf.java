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

public class CreateContactHistoryJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/CreateContactHistoryJob")
		    	.withDescription("冻结目标客群（创建联络历史清单）; 参数：id=T_ACT_TARGET表的主键值, userId=当前登录人userId")
		        .withIdentity("CreateContactHistoryJob", jobGroup)
		        .build();
		}
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
	    // Trigger the job to run now
	    Trigger trigger = newTrigger()
	        .withIdentity("CreateContactHistoryJob-", jobGroup)
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
