package com.wanda.ccs.jobhub.member.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.MemberNextLevelService;

public class MemberNextLevelJob implements Job {

	@InstanceIn(path = "MemberNextLevelService")
	private MemberNextLevelService memberNextLevelService;
	
	private static int status = 0;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
    	try {
    		if(status == 1) {
    			throw new Exception("程序正在运行中,请稍后手动重试...");
    		}
    		status = 1;
			
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			// 按日期重新跑
			String yyyy = jobDataMap.get("date") == null ? null : jobDataMap.get("date").toString();
			if(yyyy == null || "".equals(yyyy)) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				yyyy = new SimpleDateFormat("yyyy").format(cal.getTime());
			}
			
			System.out.println("=================="+yyyy+"===================");
			// 是否存在历史计算数据
			System.out.println("delete...");
			//if(memberNextLevelService.existTempDate()) {
			memberNextLevelService.clearMemberNextLevel();
			//}
			System.out.println("calculate...");
			// 计算会员下一等级所需条件
			memberNextLevelService.calculateMemberNextLevel(yyyy);
			
			System.out.println("update...");
			// 更新会员计算结果到T_MEMBER_LEVEL表中
			memberNextLevelService.updateMemberNextLevel();
			
			// 清除历史数据
			//memberNextLevelService.clearMemberNextLevel();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		} finally {
			status = 0;
		}
	}

}
