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
		
		try {
			// 按日期重新跑
			String date = jobDataMap.get("date") == null ? null : jobDataMap.get("date").toString();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			
			if(date == null || "".equals(date)) {
				cal.setTimeInMillis(System.currentTimeMillis());
				date = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
			} else {
				String[] t = date.split("-");
				if(t.length >= 2) {
					date = t[0] + "-" + t[1];
				}
				cal.setTime(format.parse(date));
				cal.add(Calendar.MONTH, 1);
				date = format.format(cal.getTime());
			}
			
			System.out.println("==============="+date+"=====================");
			
			
			memberDNAService.cleanAllDNA();
			
			memberDNAService.calculateMemberByHuangNiu();
			
			memberDNAService.calculateMemberTicket(date);
			
			memberDNAService.calculateMemberBehaviorBase(date);
			
			memberDNAService.calculateMemberBehaviorIndex();
			
			memberDNAService.calculateMemberBehaviorStand();
			
			memberDNAService.updateMemberBehaviorStand();
			
			memberDNAService.calculateMemberBehaviorDistance();
			
			memberDNAService.calculateMemberBehaviorSegment(date);
			
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
