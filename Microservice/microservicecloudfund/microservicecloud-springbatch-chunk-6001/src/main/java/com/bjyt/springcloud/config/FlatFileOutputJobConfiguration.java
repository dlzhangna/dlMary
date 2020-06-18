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
public class FlatFileOutputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private ItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	@Qualifier("flatFileCustomerOutputWriter")
	private ItemWriter<? super Customer> flatFileCustomerOutputWriter;

	@Bean
	public Job flatFileOutputJob() {
		return jobBuilderFactory.get("flatFileOutputJob")
			.start(flatFileOutputStep())
			.build();
	}
    
	@Bean
	public Step flatFileOutputStep() {
		return stepBuilderFactory.get("flatFileOutputStep")
				.<Customer,Customer>chunk(5)
				.reader(dbJdbcCustomerInputReader)
				.writer(flatFileCustomerOutputWriter)
				.build();
	}

}
