package com.bjyt.springcloud.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.reader.SimpleListReader;

@Configuration
public class SimpleJobConfiguration {
	 @Autowired
	 public JobBuilderFactory jobBuilderFactory;

	 @Autowired
	 public StepBuilderFactory stepBuilderFactory;
	 
	 @Bean
	 public Job simpleListJob() {
		 return jobBuilderFactory.get("simpleListJob")
				.start(simpleListStep())
				.build();
	 }

	private Step simpleListStep() {
		return stepBuilderFactory.get("simpleListStep")
				.chunk(2)
				.reader(simpleListReader())
				.writer(list->{
					for(Object item:list) {
						System.out.println("current item is: " + item);
					}
				}).build();
	}
    
	@Bean
	public SimpleListReader simpleListReader() {
		List<String> data = Arrays.asList("one","two","three");
		return new SimpleListReader(data);
	}

}
