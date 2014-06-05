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

@ContextConfiguration(locations={"/META-INF/jobs/job-mbr-degrade.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MbrDegradeTests{
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job mbrDegradeByYear;
	
	@Test
	public void testMbrDegradeByYearJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
		.addString("year", "2013")
		.addString("output.folder", "./levelDegradefiles")
		.toJobParameters();
		
		JobExecution execution = jobLauncher.run(mbrDegradeByYear, jobParameters);
		Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}
	
}
