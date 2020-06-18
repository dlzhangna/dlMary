package com.bjyt.springcloud.JobLauncher;


import java.util.Date;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

@Component("scheduledJobIncrementor")
public class ScheduledJobIncrementor implements JobParametersIncrementer{

	@Override
	public JobParameters getNext(JobParameters parameters) {
		JobParameters jobParameters = (parameters==null)? new JobParameters(): parameters;
		return new JobParametersBuilder(jobParameters).addDate("scheduledParam",new Date()).toJobParameters();
	}

}
