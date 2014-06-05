package com.wanda.ccs.jobhub.smsreport.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.service.SendCinemaMemberSmsService;
import com.wanda.ccs.jobhub.smsreport.service.SendLineMemberSmsService;

//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class SendMemberSMSJob implements Job {

	@InstanceIn(path="SendCinemaMemberSmsService")
	private SendCinemaMemberSmsService sendCinemaMemberSmsService;
	
	@InstanceIn(path="SendLineMemberSmsService")
	private SendLineMemberSmsService sendLineMemberSmsService;
	
	public final static int SMS_STATUS_RUNNING = 1;
	public final static int SMS_STATUS_WAITTING = 0;
	public final static String SMS_STATUS = "SEND_STATUS";
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			run(context);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
	}
	
	private void run(JobExecutionContext context) throws Exception {
		JobDataMap dateMap = null;
		
		try {
			dateMap = context.getJobDetail().getJobDataMap();
			int status = dateMap.getInt(SendMemberSMSJob.SMS_STATUS);
			System.out.println("status --> " + (status==1?"running":"waitting"));
			if(SendMemberSMSJob.SMS_STATUS_RUNNING == status) {
				context.setResult("当前会员手机报程序正在运行中。。");
				return ;
			}
			dateMap.put(SendMemberSMSJob.SMS_STATUS, SendMemberSMSJob.SMS_STATUS_RUNNING);
			
			Calendar cal = Calendar.getInstance();
			int x = -1;
			cal.add(Calendar.DAY_OF_MONTH, x);
			final String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		    String[] time = date.split("-");
			int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
			
			// 验证当前会员手机报是否手动执行
			String triggerName = "ForceSendMemberSmsJobTrigger-"+date;
			if(context.getScheduler().getTriggerGroupNames().size() > 0) {
				String groupName = context.getScheduler().getTriggerGroupNames().get(0);
				Trigger trigger = context.getScheduler().getTrigger(new TriggerKey(triggerName, groupName));
				if(trigger != null) {
					throw new Exception("sended 会员手机报程序已经手动触发执行，暂不处理");
				}
			}
			
			// 开始统计会员手机报
			Map<String, Integer> flag = sendLineMemberSmsService.getStatus(yyyyMMdd);
			if(flag != null && flag.size() > 0) {
				final CountDownLatch latch = new CountDownLatch(flag.size());
				final Map<String, Exception> map = new HashMap<String, Exception>(flag.size());
				// 统计院线短信
				if(flag.get("FLAG_SMS") != null && flag.get("FLAG_SMS") == 0) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								sendLineMemberSmsService.sendLineSms(date);
							} catch (Exception e) {
								map.put("line", e);
							} finally {
								latch.countDown();
							}
						}
					}).start();
				}
				
				// 统计影城短信
				if(flag.get("FLAG_CINEMA_SMS") != null && flag.get("FLAG_CINEMA_SMS") == 0) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								sendCinemaMemberSmsService.sendCinemaSms(date);
							} catch (Exception e) {
								map.put("cinema", e);
							} finally {
								latch.countDown();
							}
						}
					}).start();
				}
				
				latch.await(60, TimeUnit.MINUTES);
				// 统计出现错误
				if(map.size() > 0) {
					throw map.get(0);
				}
				
				if(flag.get("FLAG_SMS") != null && flag.get("FLAG_SMS") == 0) {
					sendLineMemberSmsService.buildLineSend(yyyyMMdd);
				}
				Thread.sleep(2000);
				if(flag.get("FLAG_CINEMA_SMS") != null && flag.get("FLAG_CINEMA_SMS") == 0) {
					sendCinemaMemberSmsService.buildCinemaSend(yyyyMMdd);
				}
			}
			//if(sendLineMemberSmsService.checkStatus(yyyyMMdd, "")) {
				
			//}
			
			/*if(sendLineMemberSmsService.checkStatus(yyyyMMdd, " and FLAG_SMS=0")) {
				sendLineMemberSmsService.sendLineSms(date);
			}
			
			if(sendCinemaMemberSmsService.checkStatus(yyyyMMdd, " and FLAG_SMS=1 and FLAG_CINEMA_SMS=0")) {
				sendCinemaMemberSmsService.sendCinemaSms(date);
			}*/
			
			
			dateMap.put(SendMemberSMSJob.SMS_STATUS, SendMemberSMSJob.SMS_STATUS_WAITTING);
		} catch (Exception e) {
			dateMap.put(SendMemberSMSJob.SMS_STATUS, SendMemberSMSJob.SMS_STATUS_WAITTING);
			throw e;
		}
	}

}
