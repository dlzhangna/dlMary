package com.bjyt.springcloud.config.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener{
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + " before running...");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + "after running...");
	}
}
