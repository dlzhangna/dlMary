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
public class MultipleFileCompositeOutputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private ItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	@Qualifier("customerCompositeItemWriter")
	private ItemWriter<? super Customer> customerCompositeItemWriter;

	@Bean
	public Job multipleFileCompositeOutputJob() {
		return jobBuilderFactory.get("multipleFileCompositeOutputJob")
			.start(multipleFileCompositeOutputStep())
			.build();
	}
    
	@Bean
	public Step multipleFileCompositeOutputStep() {
		return stepBuilderFactory.get("multipleFileCompositeOutputStep")
				.<Customer,Customer>chunk(5)
				.reader(dbJdbcCustomerInputReader)
				.writer(customerCompositeItemWriter)
				.build();
	}

}
