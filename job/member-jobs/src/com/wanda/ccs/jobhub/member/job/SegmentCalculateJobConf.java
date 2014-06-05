package com.wanda.ccs.jobhub.member.job;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.google.code.pathlet.quartz.PathletVirtualJob;

public class SegmentCalculateJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(PathletVirtualJob.class)
				.withDescription("客群计算任务，其中包含计算客群总数和客群落地两步。参数：id=T_SEGMENT表中的客群Id")
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/member/SegmentCalculateJob")
		        .withIdentity("SegmentCalculateJob", jobGroup)
		        .build();
		}
		return job;
	}

	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		String segmentId = triggerDataMap.get("id");
		
	    // Trigger the job start at 5 seconds later
	    Trigger trigger = newTrigger()
	        .withIdentity("SegmentCalculateJob-" + segmentId, jobGroup)
	        .startAt(futureDate(5, IntervalUnit.SECOND)) 
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
