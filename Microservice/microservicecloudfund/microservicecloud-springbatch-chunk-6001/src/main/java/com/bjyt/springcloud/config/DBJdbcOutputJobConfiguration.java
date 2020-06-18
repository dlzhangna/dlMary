package com.bjyt.springcloud.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.entity.Customer;

@Configuration
public class DBJdbcOutputJobConfiguration {
  
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("flatFileCustomerInputToDBReader")
	private ItemReader<Customer> flatFileCustomerInputToDBReader;
	
	@Autowired
	@Qualifier("dbCustomerOutputFlatFileWriter")
	private JdbcBatchItemWriter<Customer> dbCustomerOutputFlatFileWriter;

	@Bean
	public Job dBJdbcOutputJob() {
		return jobBuilderFactory.get("dBJdbcOutputJob")
			.start(dbJdbcOutputStep())
			.build();
	}
    
	@Bean
	public Step dbJdbcOutputStep() {
		return stepBuilderFactory.get("dbJdbcOutputStep")
				.<Customer,Customer>chunk(100)
				.reader(flatFileCustomerInputToDBReader)
				.writer(dbCustomerOutputFlatFileWriter)
				.build();
	}
}
