package com.wanda.ccs.jobhub.member.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.CreateContactHistoryService;
import com.wanda.ccs.jobhub.member.service.SegmentService;
import com.wanda.ccs.jobhub.member.vo.SegmentVo;
import com.wanda.ccs.jobhub.member.vo.TargetVo;
/**
 * 冻结客群，创建联络清单
 * @author YangJianbin
 *
 */
public class CreateContactHistoryJob implements Job {
	
	@InstanceIn(path = "CreateContactHistoryService")
	private CreateContactHistoryService createContactHistoryService;
	
	@InstanceIn(path = "SegmentService")
	private SegmentService segmentService;
	

	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	    Map<String, Object> params = context.getTrigger().getJobDataMap();
		String userId = params.get("userId") == null? "":params.get("userId").toString();
		Long actTargetId = params.get("id") == null||params.get("id").equals("") ? 0L:Long.valueOf(params.get("id").toString());
		TargetVo targetVo = createContactHistoryService.getTargetVo(actTargetId);
	    
		Long segmentId = targetVo.getSegmentId();
		boolean lockedByThis = false;
		
	    try {
			lockedByThis = segmentService.updateLockSegment(segmentId, SegmentVo.OCCUPIED_BY_ACT_TARGET);
			if(lockedByThis) {
				createContactHistoryService.saveContactHistory(targetVo, userId);
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
				segmentService.updateUnlockSegment(segmentId, SegmentVo.OCCUPIED_BY_ACT_TARGET);
			}
		}
	    
	}

/*	public void execute(JobExecutionContext context){
		
		String segmentIdStr = context.getTrigger().getJobDataMap().get("id").toString().trim();
		Long segmentId = Long.parseLong(segmentIdStr);
		
		boolean lockedByThis = false;
		try {
			this.logHelper.info("Trigger is starting, Key:" + context.getTrigger().getKey() + ", segmentId=" + segmentId);
			
			lockedByThis = segmentService.lockSegment(segmentId, SegmentVo.OCCUPIED_BY_CAL_COUNT);
			if(lockedByThis) {
				Long count = segmentService.updateCalCount(segmentId, DEFAULT_QUERY_TIMEOUT);
				this.logHelper.info("Trigger '" + context.getTrigger().getKey()  + "' : Segment count has completely calculated. " + 
						 "The new count of segment(segmentId=" + segmentId + "， count=" + count + ")");
			}
			else {
				this.logHelper.warn("Trigger '" + context.getTrigger().getKey()  + "'skipped! This segment has been occupied! " + 
						 "segmentId=" + segmentId);
			}
		}
		catch(Throwable t) {
			this.logHelper.info("Trigger '" + context.getTrigger().getKey()  + "' execution error!", t);
		}
		finally {
			//release lock
			if(lockedByThis) {
				segmentService.unlockSegment(segmentId, SegmentVo.OCCUPIED_BY_CAL_COUNT);
			}
		}
	}
*/

}
