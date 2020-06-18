package com.bjyt.springcloud.JobLauncher;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobOperatorJobConfiguration  implements StepExecutionListener,ApplicationContextAware {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	private Map<String,JobParameter> jobParams;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	private ApplicationContext context;
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception{
		JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
		postProcessor.setJobRegistry(jobRegistry);
		postProcessor.setBeanFactory(context.getAutowireCapableBeanFactory());
		postProcessor.afterPropertiesSet();
		return postProcessor;
	}
	
	@Bean
	public JobOperator jobOperator() {
		SimpleJobOperator operator = new SimpleJobOperator();
		operator.setJobLauncher(jobLauncher);
		operator.setJobParametersConverter(new DefaultJobParametersConverter());
		operator.setJobRepository(jobRepository);
		operator.setJobExplorer(jobExplorer);
		operator.setJobRegistry(jobRegistry);
		return operator;
	}
	@Bean
	public Job jobOperatorDemoJob() {
		return jobBuilderFactory.get("jobOperatorDemoJob")
			.start(jobOperatorDemoStep())
			.build();
	}
    
	@Bean
	public Step jobOperatorDemoStep() {
		return stepBuilderFactory.get("jobOperatorDemoStep")
				.listener(this)
				.tasklet(((contribution,chunkContext)->{
					System.out.println("jobOperatorDemoStep runs with param: " + jobParams.get("operatorParam").getValue());
					return RepeatStatus.FINISHED;
				})).build();
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("beforeStep");
		jobParams = stepExecution.getJobParameters().getParameters();
		System.out.println("jobOperatorParams:" + jobParams.get("operatorParam"));
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
