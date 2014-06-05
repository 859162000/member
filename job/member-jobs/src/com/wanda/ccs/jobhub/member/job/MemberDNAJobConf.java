package com.wanda.ccs.jobhub.member.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class MemberDNAJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
				.withDescription("计算会员DNA默认参数为空。重新计算2014年1月会员DNA，参数例如：{\"date\":\"2014-01\"}")
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/MemberDNAJob")
		        .withIdentity("MemberDNAJob", jobGroup)
		        .build();
		}
		
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		Trigger trigger = newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 0  1 * ?"))
				.withIdentity("MemberDNAJobTrigger", jobGroup)
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
