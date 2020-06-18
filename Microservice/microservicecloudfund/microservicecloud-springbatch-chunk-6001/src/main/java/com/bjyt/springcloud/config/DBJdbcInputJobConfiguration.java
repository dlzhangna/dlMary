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
public class DBJdbcInputJobConfiguration {
  
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
//	@Autowired
//	private DataSource dataSource;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private ItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	@Qualifier("customerWriter")
	private ItemWriter<? super Customer> customerWriter;

	@Bean
	public Job dBJdbcInputJob() {
		return jobBuilderFactory.get("dBJdbcInputJob")
			.start(dbJdbcInputStep())
			.build();
	}
    
	@Bean
	public Step dbJdbcInputStep() {
		return stepBuilderFactory.get("dbJdbcInputStep")
				.<Customer,Customer>chunk(100)
				.reader(dbJdbcCustomerInputReader)
				.writer(customerWriter)
				.build();
	}
}
