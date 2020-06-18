package com.bjyt.springcloud.config.split;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class SplitDemo {
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  
  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  
  @Bean
  public Step splitStep1() {
	  return stepBuilderFactory.get("jobSplitStep1")
			   .tasklet(new Tasklet() {
				  @Override
				  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					  System.out.println(chunkContext.getStepContext().getStepName() + "," + Thread.currentThread().getName());
					   return RepeatStatus.FINISHED;
					}}).build();
     }
  @Bean
  public Step splitStep2() {
	  return stepBuilderFactory.get("jobSplitStep2")
			   .tasklet(new Tasklet() {
				  @Override
				  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					  System.out.println(chunkContext.getStepContext().getStepName() + "," + Thread.currentThread().getName());
					   return RepeatStatus.FINISHED;
					}}).build();
     }
  @Bean
  public Step splitStep3() {
	  return stepBuilderFactory.get("jobSplitStep3")
			   .tasklet(new Tasklet() {
				  @Override
				  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					  System.out.println(chunkContext.getStepContext().getStepName() + "," + Thread.currentThread().getName());
					   return RepeatStatus.FINISHED;
					}}).build();
     }
  @Bean
  public Flow splitDemoFlow1() {
	  return new FlowBuilder<Flow>("splitDemoFlow1")
			  .start(splitStep1())
			  .build();
   }
  @Bean
  public Flow splitDemoFlow2() {
	  return new FlowBuilder<Flow>("splitDemoFlow2")
			  .start(splitStep2())
			  .next(splitStep3())
			  .build();
   }
  @Bean
  public Job splitDemoJob() {
	  return jobBuilderFactory.get("splitDemoJob")
			  .start(splitDemoFlow1())
			  .split(new SimpleAsyncTaskExecutor())
			  .add(splitDemoFlow2())
			  .end()
			  .build();
   }
  }