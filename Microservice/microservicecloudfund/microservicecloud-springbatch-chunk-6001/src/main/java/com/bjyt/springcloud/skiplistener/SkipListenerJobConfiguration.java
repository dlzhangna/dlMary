package com.bjyt.springcloud.skiplistener;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkipListenerJobConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("skipListenerProcessor")
	private SkipListenerProcessor skipListenerProcessor;
	
	@Autowired
	@Qualifier("skipListenerWriter")
	private ItemWriter<String> skipListenerWriter;
	
	@Autowired
	@Qualifier("customerSkipListener")
	private CustomerSkipListener customerSkipListener;
	
	
	@Bean
	public Job skipListenerJob() {
		return jobBuilderFactory.get("skipListenerJob")
			.start(skipListenerStep())
			.build();
	}
	
	@Bean
	public Step skipListenerStep() {
		return stepBuilderFactory.get("skipListenerStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipListenerProcessor)
				.writer(skipListenerWriter)
				.faultTolerant()
				.skip(CustomSkipListenerableException.class)
				.skipLimit(10)
				.listener(customerSkipListener)
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
