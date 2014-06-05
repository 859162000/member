package com.wanda.ccs.jobhub.smsreport.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class ForceSendMemberSmsJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
				.withDescription("发送会员短信。参数例如：{\"date\":\"2013-07-18\",\"type\":\"line或者cinema\",\"sendType\":\"1（重新统计发送）或者2（直接发送）\"}")
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/smsreport/ForceSendMemberSmsJob")
		        .withIdentity("ForceSendMemberSmsJob", jobGroup)
		        .build();
		}
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		String date = triggerDataMap.get("date");
	    Trigger trigger = newTrigger()
	        .withIdentity("ForceSendMemberSmsJobTrigger-"+date, jobGroup)
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
