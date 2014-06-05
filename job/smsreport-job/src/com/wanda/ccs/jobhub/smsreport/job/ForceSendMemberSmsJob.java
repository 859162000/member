package com.wanda.ccs.jobhub.smsreport.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.Contact;
import com.wanda.ccs.jobhub.smsreport.service.ForceSendMemberSmsService;

//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class ForceSendMemberSmsJob implements Job {

	@InstanceIn(path="ForceSendMemberSmsService")
	private ForceSendMemberSmsService forceSendMemberSmsService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
	    String date = jobDataMap.get("date").toString();
	    String type = jobDataMap.get("type").toString();
	    int sendType = Integer.parseInt(jobDataMap.get("sendType").toString());
	    boolean valid = Contact.VALID_FALG;	//默认进行校验
	    
	    try {
	    	// 判断会员手机报程序是否正在执行
	    	for(JobExecutionContext job : context.getScheduler().getCurrentlyExecutingJobs()) {
	    		if("SendMemberSMSJob".equals(job.getJobDetail().getKey().getName())) {
					throw new Exception("会员手机报程序正在运行中,请稍后手动重试...");
				}
	    	}
			
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	Date time = sdf.parse(date);
	    	if(date.equals(sdf.format(new Date())) || time.after(new Date())) {
	    		throw new Exception("只能发送小于"+sdf.format(new Date())+"日期的会员手机报");
	    	}
	    	
	    	forceSendMemberSmsService.forceSend(date, type, sendType, valid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
	}

}
