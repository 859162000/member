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
import com.google.code.pathlet.quartz.SafePathletVirtualJob;

public class CampaignCalculateJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = {JobLoggingPhase.FAILED, JobLoggingPhase.VETOED};
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(SafePathletVirtualJob.class)
				.withDescription("营销活动统计默认参数为空。重新统计某天的所有营销活动，例如：{\"date\":\"2014-05-01\"}。重新统计某个营销活动，例如：{\"id\":\"1\"}。注意如果营销活动已经结束只能通过ID来重新统计")
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/CampaignCalculateJob")
		        .withIdentity("CampaignCalculateJob", jobGroup)
		        .build();
		}
		
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		Trigger trigger = newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 0 12 * * ?"))
				.withIdentity("CampaignCalculateJobTrigger-", jobGroup)
				.startNow()
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
