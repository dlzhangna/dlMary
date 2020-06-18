package com.bjyt.springcloud.retry;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;

@Configuration
public class RetryJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("retryProcessor")
	private ItemProcessor retryProcessor;
	
	@Autowired
	@Qualifier("retryWriter")
	private ItemWriter<String> retryWriter;
	
	@Bean
	public Job retryJob() {
		return jobBuilderFactory.get("retryJob")
			.start(retryStep())
			.build();
	}
	
	@Bean
	public Step retryStep() {
		return stepBuilderFactory.get("retryStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(retryProcessor)
				.writer(retryWriter)
				.faultTolerant()
				.retry(CustomRetryableException.class)
				.retryLimit(10)
				.build(); 
	}
	
	@Bean
	@StepScope
	public ListItemReader reader() {
		List<String> items = new ArrayList<>();
		
		for(int i=0;i < 50; i++) {
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader(items);
		
		return reader;
	}

}
