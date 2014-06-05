package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.ChangeMemberPointService;
import com.wanda.ccs.jobhub.member.service.PointOperationService;

/**
 * 仅用户2013年底积分清零操作。
 * @author clzhang
 *
 */
public class PointExpire2013Job implements Job {

	@InstanceIn(path="PointOperationService")
	private PointOperationService pointOperationService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
		pointOperationService.expire2013();
	}

}
