package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.ImportExcelMemberService;

/**
 * 上传波次目标文件
 * @author YangJianbin
 *
 */
public class ImportExcelMemberJob implements Job {
	
	@InstanceIn(path="ImportExcelMemberService")
	private ImportExcelMemberService importExcelMemberService;
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap params = context.getTrigger().getJobDataMap();
			Long fileId = Long.parseLong((String)params.get("fileAttachId"));
			String userId = (String)params.get("userId");
			
			importExcelMemberService.execute(fileId, "TCmnActivity", userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
	}

}
