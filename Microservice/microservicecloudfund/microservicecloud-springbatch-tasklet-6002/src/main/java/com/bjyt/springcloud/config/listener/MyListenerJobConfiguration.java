package com.bjyt.springcloud.config.listener;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyListenerJobConfiguration {
  
	@Autowired
	private JobBuilderFactory jobBuildrFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;;
	
	@Bean
	public Job myListenerJob() {
		return jobBuildrFactory.get("myListenerJob")
				.start(myListenerStep())
				.listener(new MyJobListener())
				.build();
	}
	
	@Bean
	public Step myListenerStep() {
		return stepBuilderFactory.get("myListenerStep")
			.chunk(2)
			.faultTolerant()
			.listener(new MyChunkListener())
			.reader(reader())
			.writer(writer())
			.build();
	}
	
	@Bean
	public ItemReader<String> reader() {
		return new ListItemReader<>(Arrays.asList("first","second","third"));
	}
    
	@Bean
	public ItemWriter<String> writer() {
		return new ItemWriter<String>() {
			@Override
			public void write(List<? extends String> items) throws Exception {
				for(String item:items) {
					System.out.println("Writing item: " + item);
				}
			}
		};
	}
    
	
}
