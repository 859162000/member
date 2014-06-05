package com.wanda.ccs.jobhub.smsreport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;

import com.google.code.pathlet.core.Module;
import com.google.code.pathlet.core.ModuleListener;
import com.google.code.pathlet.core.PathletContainer;
import com.google.code.pathlet.quartz.JobConfig;
import com.google.code.pathlet.quartz.admin.service.InstanceService;
import com.google.code.pathlet.quartz.admin.service.JobConfigService;
import com.wanda.ccs.jobhub.smsreport.job.ForceSendMemberSmsJobConf;
import com.wanda.ccs.jobhub.smsreport.job.SendMemberSMSJobConf;

public class SmsreportJobModuleListener implements ModuleListener {
	
	private Log log = LogFactory.getLog(SmsreportJobModuleListener.class);
	
	private Class[] jobConfigs = {
			ForceSendMemberSmsJobConf.class,
			SendMemberSMSJobConf.class
	};
	
	public void init(PathletContainer container, Module module) {
		log.info("Module listener initializing[moduleId=" + module.getId() + "]");
		JobConfigService jobConfigService = (JobConfigService)container.getInstance("/admin/JobConfigService");
		InstanceService instanceService = (InstanceService)container.getInstance("/admin/InstanceService");
		try {
			boolean isMaster = instanceService.getMyInstance().isMaster();
			System.out.println("isMaster:"+isMaster);
			int removedJobCount = jobConfigService.removeJobs(module.getId(), isMaster);
			if(removedJobCount > 0) {
				log.warn("Module[moduleId=" + module.getId() + "] jobs has be cleaned! removed jobs count=" + removedJobCount);
			}
			
			for(Class jobConfigClass : jobConfigs) {
				JobConfig config = (JobConfig)jobConfigClass.newInstance();
				config.setJobGroup(module.getId());
				
				if(isMaster) {					
					jobConfigService.addJob(config, true);
					if(config.scheduleOnStart()) {
						jobConfigService.scheduleJob(config.getJobDetail().getKey());
					}
				} else {
					jobConfigService.addJob(config, false);
				}
			}
			
		}
		catch (Exception e) {
			log.error("Faile to initailize module: " + module.getId(), e);
		}
		
	}

	public void destroy(PathletContainer container, Module module) {
		JobConfigService jobConfigService = (JobConfigService)container.getInstance("/admin/JobConfigService");
		InstanceService instanceService = (InstanceService)container.getInstance("/admin/InstanceService");
		int removedJobCount = 0;
		try {
			boolean isMaster = instanceService.getMyInstance().isMaster();
			removedJobCount = jobConfigService.removeJobs(module.getId(), isMaster);
		} catch (SchedulerException e) {
			log.error("Error in module destory! moduleId: " + module.getId(), e);
		}
		
		log.info("Module listener destoried[moduleId=" + module.getId() + "], Removed jobs count=" + removedJobCount);
	}

}
