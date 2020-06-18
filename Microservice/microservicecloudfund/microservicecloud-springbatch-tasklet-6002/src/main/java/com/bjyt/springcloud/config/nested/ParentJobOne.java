package com.bjyt.springcloud.config.nested;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ParentJobOne {
  
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private Job childJob1;
	
	@Autowired
	private Job childJob2;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	public Job parentJob(JobRepository repository, PlatformTransactionManager transactionManager) {
		return jobBuilderFactory.get("parentJob")
				.start(childJob1(repository,transactionManager))
				.next(childJob2(repository,transactionManager))
				.build();
	}

	
	//return jobStep
	//decorator pattern->io:Buffered Reader
	private Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob1"))
				.job(childJob1)
				.launcher(jobLauncher)
				.repository(repository)
				.transactionManager(transactionManager)
				.build();
	}
	
	private Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob2"))
				.job(childJob2)
				.launcher(jobLauncher)
				.repository(repository)
				.transactionManager(transactionManager)
				.build();
	}
}
