package com.bjyt.springcloud.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.entity.Customer;


@Configuration
public class FlatFileInputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("flatFileCustomerInputReader")
	private ItemReader<Customer> flatFileCustomerInputReader;
	
	@Autowired
	@Qualifier("customerWriter")
	private ItemWriter<? super Customer> customerWriter;

	@Bean
	public Job flatFileInputJob() {
		return jobBuilderFactory.get("flatFileInputJob")
			.start(flatFileInputStep())
			.build();
	}
    
	@Bean
	public Step flatFileInputStep() {
		return stepBuilderFactory.get("flatFileStep")
				.<Customer,Customer>chunk(100)
				.reader(flatFileCustomerInputReader)
				.writer(customerWriter)
				.build();
	}

}
