package com.wanda.member.activity;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations={"/META-INF/jobs/job-activity-point.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ActivityPointTests{
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job activityPointByDate;
	
	@Autowired
	private Job runTicketRuleByDate;
	@Autowired
	private Job runMemberRuleByDate;
	@Autowired
	private Job runGoodsRuleByDate;
	@Autowired
	private Job rollbackAllPointByDate;
	@Autowired
	private Job rollbackMemberPointByDate;
	@Autowired
	private Job getRuleInfo;
	
	@Autowired
	private SqlSessionTemplate mbrSqlSession;
	
	
	@Test
	public void testActivityPointByDateJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-09-17")
		.addString("rule_id", "486")
		.addString("output.folder", "./activityfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(activityPointByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testRunTicketRuleByDate() throws Exception{
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-10-23")
		.addString("output.folder", "./activityfiles")
		.addString("member_id", "24287")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(runTicketRuleByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testRunGoodsRuleByDate() throws Exception{
			JobParameters jobParameters = new JobParametersBuilder()
			.addString("biz_date", "2013-10-23")
			.addString("output.folder", "./activityfiles")
			.toJobParameters();
			
			JobExecution execution = jobLauncher.run(runGoodsRuleByDate, jobParameters);
			Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
		}
	
	//@Test
	public void testRunMemberRule() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-09-15")
		.addString("rule_id", "486")
		.addString("output.folder", "./activityfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(runMemberRuleByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testRollbackAllPointByDate() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-10-21")
		.addString("output.folder", "./activityfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(rollbackAllPointByDate, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
	//@Test
	public void testRollbackMemberPointByDate() throws Exception {
			JobParameters jobParameters = new JobParametersBuilder()
			.addString("biz_date", "2013-10-01")
			.addString("output.folder", "./activityfiles")
			.toJobParameters();
			
			JobExecution execution = jobLauncher.run(rollbackMemberPointByDate, jobParameters);
			Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
		}
	//@Test
	public void testGetRuleInfo() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("biz_date", "2013-09-16")
		.addString("rule_id", "545")
		.addString("output.folder", "./activityfiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(getRuleInfo, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
}
