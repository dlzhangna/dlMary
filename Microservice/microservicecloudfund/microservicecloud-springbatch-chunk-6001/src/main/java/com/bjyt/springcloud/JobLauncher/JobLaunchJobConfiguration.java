package com.bjyt.springcloud.JobLauncher;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobLaunchJobConfiguration  implements StepExecutionListener {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
//	@Autowired
//	private JobRepository jobRepository;
	
	private Map<String,JobParameter> jobParams;
	
	@Bean
	public Job jobLaunchDemoJob() {
		return jobBuilderFactory.get("jobLaunchDemoJob")
			.start(jobLaunchDemoStep())
			.build();
	}
    
	@Bean
	public Step jobLaunchDemoStep() {
		return stepBuilderFactory.get("jobLaunchDemoStep")
				.listener(this)
				.tasklet(((contribution,chunkContext)->{
					System.out.println("jobLaunchDemoStep runs with param: " + jobParams.get("jobParam").getValue());
					return RepeatStatus.FINISHED;
				})).build();
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("beforeStep");
		jobParams = stepExecution.getJobParameters().getParameters();
		System.out.println("jobParams:" + jobParams.get("jobParam"));
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}
