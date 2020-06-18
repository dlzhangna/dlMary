package com.bjyt.springcloud.errorhanding;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableBatchProcessing
public class ErrorHandingJobConfiguration {
  
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job errorHandingJob() {
		return jobBuilderFactory.get("errorHandingJob")
			.start(errorHandingStep1())
			.build();
	}
    
	@Bean
	public Step errorHandingStep1() {
		return stepBuilderFactory.get("errorHandingStep1")
				.tasklet(errorHandlingTasklet())
				.build();
	}
	
	@Bean
	public Step errorHandingStep2() {
		return stepBuilderFactory.get("errorHandingStep2")
				.tasklet(errorHandlingTasklet())
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet errorHandlingTasklet() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Map<String,Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
				
				if(stepExecutionContext.containsKey("first")) {
					System.out.println("Second run will success...");
					return RepeatStatus.FINISHED;
				}else {
					System.out.println("First run will fail...");
					chunkContext.getStepContext().getStepExecution().getExecutionContext().put("first", true);
					throw new RuntimeException("Error occurs...");
				}
			}
	    };
    }
}
