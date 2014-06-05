package com.wanda.member.basic;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"/META-INF/jobs/job-basic-point.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicPointTests{
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job basicPointByDate;
	@Autowired
	private Job basicTicketPointByDate;
	
	@Autowired
	private Job mbr52Todw13;
	@Autowired
	private Job checkStatusJob;
	@Autowired
	private Job updateStatusJob;
	
	//@Test
	public void testBasicPointByDateJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2014-01-20")
		.addString("output.folder", "./basicPointfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(basicPointByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testTicketPointByDateJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-09-10")
		.addString("output.folder", "./basicPointfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(basicTicketPointByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	@Test
	public void testMbr52Todw13Job() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2014-02-07")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(mbr52Todw13, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testCheckStatusJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2014-02-06")
		.addString("check_status", "on")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(checkStatusJob, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	//@Test
	public void testUpdateStatusJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2014-02-15")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(updateStatusJob, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
}
