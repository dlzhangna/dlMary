package com.bjyt.springcloud.JobLauncher;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class JobScheduledJobConfiguration  implements StepExecutionListener,ApplicationContextAware {
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
	
	@Autowired
	private JobParametersIncrementer scheduledJobIncrementor;
	
	@Scheduled(fixedDelay = 5000)
	public void scheduler() throws NoSuchJobException, JobParametersNotFoundException, JobRestartException, JobExecutionAlreadyRunningException, JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException, JobParametersInvalidException {
		jobOperator().startNextInstance("jobScheduledDemoJob");
	}
	
	@Bean
	public Job jobScheduledDemoJob() {
		return jobBuilderFactory.get("jobScheduledDemoJob")
			.incrementer(scheduledJobIncrementor)
			.start(jobScheduledDemoStep())
			.build();
	}
	
	@Bean
	public Step jobScheduledDemoStep() {
		return stepBuilderFactory.get("jobScheduledDemoStep")
				.listener(this)
				.tasklet(((contribution,chunkContext)->{
					System.out.println("jobScheduledDemoStep runs with param: " + jobParams.get("scheduledParam").getValue());
					return RepeatStatus.FINISHED;
				})).build();
	}

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
	
    
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("beforeStep");
		jobParams = stepExecution.getJobParameters().getParameters();
		String str = jobParams.get("scheduledParam").toString();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//		Date strDate = new Date();
//		try {
//			strDate = simpleDateFormat.parse(str);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("===========scheduledParam=============" + str);
//		System.out.println(strDate.toString());
		
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
