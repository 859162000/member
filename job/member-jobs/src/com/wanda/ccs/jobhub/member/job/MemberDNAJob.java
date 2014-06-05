package com.wanda.ccs.jobhub.member.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.service.MemberDNAService;

public class MemberDNAJob implements Job {

	@InstanceIn(path = "MemberDNAService")
	private MemberDNAService memberDNAService;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
		
		// 按日期重新跑
		String date = jobDataMap.get("date") == null ? null : jobDataMap.get("date").toString();
		if(date == null || "".equals(date)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			date = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
		}
		
		System.out.println("==============="+date+"=====================");
		String[] dates = date.split("-");
		String yyyyMM = dates[0]+""+dates[1];
		
		try {
			execute(yyyyMM);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}
	
	private void execute(String date) throws Exception {
		
		//memberDNAService.cleanAllDNA();
		
		//memberDNAService.calculateMemberByHuangNiu();
		
		//memberDNAService.calculateMemberTicket(date);
		
		//memberDNAService.calculateMemberBehaviorBase(date);
		
		//memberDNAService.calculateMemberBehaviorIndex();
		
		//memberDNAService.calculateMemberBehaviorStand();
		
		//memberDNAService.updateMemberBehaviorStand();
		
		//memberDNAService.calculateMemberBehaviorDistance();
		
		//memberDNAService.calculateMemberBehaviorSegment(date);
		
	}

}
