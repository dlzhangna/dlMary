package com.bjyt.springcloud.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobDemo {
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  
  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  
  @Bean
  public Job demoJob() {
	  return jobBuilderFactory.get("demoJob")
			 .start(step1())
			 .next(step2())
			 .next(step3())
//			  .start(step1())
//			  .on("COMPLETED")
//			  .to(step2())
//			  .from(step2())
//			  .on("COMPLETED")//.fail().stopAndRestart(flow)
//			  .to(step3())
//			  .from(step3())
//			  .end()
			  .build();
  }
  
  @Bean
  public Step step1() {
	  return stepBuilderFactory.get("step1")
			 .tasklet(new Tasklet() {

				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("step1");
					return RepeatStatus.FINISHED;
				}
				 
			 }).build();
  }
  
  @Bean
  public Step step2() {
	  return stepBuilderFactory.get("step2")
				 .tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step2");
						return RepeatStatus.FINISHED;
					}
					 
				 }).build();
  }
  
  @Bean
  public Step step3() {
	  return stepBuilderFactory.get("step3")
				 .tasklet(new Tasklet() {

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step3");
						return RepeatStatus.FINISHED;
					}
					 
				 }).build();
  }
}
