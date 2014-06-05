package com.wanda.ccs.jobhub.smsreport.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.JobLoggingPhase;
import com.google.code.pathlet.quartz.SafePathletVirtualJob;
import com.google.code.pathlet.quartz.PathletJobFactory;
import com.wanda.ccs.jobhub.smsreport.util.PropertiesUtil;

public class SendMemberSMSJobConf implements JobConfig {
	
	private String jobGroup;
	
	private JobDetail job = null;
	
	private JobLoggingPhase[] phases = ALL_PHASES;
	
	public JobDetail getJobDetail() {
		if(job == null) {
			job = newJob(SafePathletVirtualJob.class)
		    	.usingJobData(PathletJobFactory.JOB_PATH_KEY, "/jobhub/smsreport/SendMemberSMSJob")
		    	.withDescription("发送会员手机报；参数： date=yyyy-MM-dd。")
		        .withIdentity("SendMemberSMSJob", jobGroup)
		        .build();
		}
		
		// 初始化发送状态，此状态会在重启服务器加载jobDetail时初始化
		job.getJobDataMap().put(SendMemberSMSJob.SMS_STATUS, SendMemberSMSJob.SMS_STATUS_WAITTING);
		
		return job;
	}
	
	public Trigger getTrigger(Map<String, String> triggerDataMap) {
		String cronTime = PropertiesUtil.getValueByKey("cronTime");
		Trigger trigger = newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronTime))
					.withIdentity("SendMemberSMSJobTrigger-", jobGroup)
					.startNow()
					.build();
		
//		Trigger trigger = newTrigger()
//		        .withIdentity("SendMemberSMSJobTrigger-", jobGroup)
//		        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//		                .withIntervalInSeconds(5)
//		                .repeatForever())    
//		        .startNow()
//		        .build();
	    
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
