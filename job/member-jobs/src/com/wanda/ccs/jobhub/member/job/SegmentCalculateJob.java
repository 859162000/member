package com.wanda.ccs.jobhub.member.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.SegmentService;
import com.wanda.ccs.jobhub.member.vo.SegmentVo;
/**
 * 计算客群数量
 * @author Charlie Zhang
 *
 */
public class SegmentCalculateJob implements Job {
	
	public final static int DEFAULT_QUERY_TIMEOUT = 60 * 90; //计算客群数量的超时时间单位秒

	@InstanceIn(path = "SegmentService")
	private SegmentService segmentService;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String segmentIdStr = context.getTrigger().getJobDataMap().get("id").toString().trim();
		Long segmentId = Long.parseLong(segmentIdStr);
		
		boolean lockedByThis = false;
		try {
			lockedByThis = segmentService.updateLockSegment(segmentId, SegmentVo.OCCUPIED_BY_CAL_COUNT);
			if(lockedByThis) {
				Long count = segmentService.updateCalCount(segmentId, DEFAULT_QUERY_TIMEOUT);
				context.setResult("Calculation completed! The new segmentCount=" + count);
			}
			else {
				context.setResult("This segment has been occupied! Try again 5 minutes later.");
				if(context.getRefireCount() <= 3) {
					Thread.sleep(1000 * 60 * 5); //间隔3分钟时间。
					throw new JobExecutionException(true);
				}
				else {
					context.setResult("This segment has been occupied! Try 5 times and still occupied!");
					throw new JobExecutionException();
				}
			}
		}
		catch(InterruptedException ie) {
			throw new JobExecutionException(ie);
		} 
		finally {
			//release lock
			if(lockedByThis) {
				segmentService.updateUnlockSegment(segmentId, SegmentVo.OCCUPIED_BY_CAL_COUNT);
			}
		}
	}


}
