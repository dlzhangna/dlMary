package com.bjyt.springcloud.config.decider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DeciderDemo {
    
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step deciderDemoStep1() {
		return stepBuilderFactory.get("deciderDemoStep1")
			.tasklet(new Tasklet() {

				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("deciderDemoStep1");
					return RepeatStatus.FINISHED;
				}
			}).build();
	}
	
	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep")
			.tasklet(new Tasklet() {

				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("evenStep");
					return RepeatStatus.FINISHED;
				}
			}).build();
	}
	
	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep")
			.tasklet(new Tasklet() {

				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("oddStep");
					return RepeatStatus.FINISHED;
				}
			}).build();
	}
   //Create Decider
   @Bean
   public JobExecutionDecider myDecider() {
	   return new MyDecider();
   }
   
   @Bean
   public Job flowDecisonDemoJob() {
	   return jobBuilderFactory.get("flowDecisonDemoJob")
			  .start(deciderDemoStep1())
			  .next(myDecider())
			  .from(myDecider()).on("even").to(evenStep())
			  .from(myDecider()).on("odd").to(oddStep())
			  .from(oddStep()).on("*").to(myDecider())
			  .end()
			  .build();
   }
}
