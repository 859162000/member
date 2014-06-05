package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.SegmentService;


/**
 * 客群导出任务
 * 把指定客群的会员信息导出为Excel文件并存入数据库，以便客户之后下载。
 * @author Charlie Zhang
 *
 */
public class SegmentExportJob implements Job{
	
	public final static int DEFAULT_QUERY_TIMEOUT = 60 * 3; //计算客群数量的超时时间单位秒

	@InstanceIn(path = "SegmentService")
	private SegmentService segmentService;

	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String idStr = (String)context.getTrigger().getJobDataMap().get("id");
		Long segmentExportId = Long.parseLong(idStr);
		
		Integer exportedRows = segmentService.updateExport(segmentExportId, DEFAULT_QUERY_TIMEOUT * 5);
		context.setResult("Exported row count=" + exportedRows);
	}


}
