package com.bjyt.springcloud.skip;

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
public class SkipJobConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("skipProcessor")
	private SkipProcessor skipProcessor;
	
	@Autowired
	@Qualifier("skipWriter")
	private ItemWriter<String> skipWriter;
	
	@Bean
	public Job skipJob() {
		return jobBuilderFactory.get("skipJob")
			.start(skipStep())
			.build();
	}
	
	@Bean
	public Step skipStep() {
		return stepBuilderFactory.get("skipStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipProcessor)
				.writer(skipWriter)
				.faultTolerant()
				.skip(CustomSkipableException.class)
				.skipLimit(10)
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
