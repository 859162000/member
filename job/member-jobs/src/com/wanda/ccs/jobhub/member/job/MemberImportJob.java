package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.MemberService;

/**
 * 批量导入会员信息。
 * @author Charlie Zhang
 *
 */
public class MemberImportJob implements Job{

	@InstanceIn(path="MemberService")
	private MemberService memberService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			Long fileId = Long.parseLong(jobDataMap.get("fileId").toString());
			memberService.execute(fileId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new JobExecutionException(e.getMessage());
		}
	}

}
