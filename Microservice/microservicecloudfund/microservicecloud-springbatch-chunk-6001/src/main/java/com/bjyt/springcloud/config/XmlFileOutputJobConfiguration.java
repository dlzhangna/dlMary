package com.bjyt.springcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import com.bjyt.springcloud.entity.Customer;


@Configuration
public class XmlFileOutputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private ItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	@Qualifier("xmlCustomerOutputWriter")
	private ItemWriter<? super Customer> xmlCustomerOutputWriter;

	@Bean
	public Job xmlFileOutputJob() {
		return jobBuilderFactory.get("xmlFileOutputJob")
			.start(xmlFileOutputStep())
			.build();
	}
    
	@Bean
	public Step xmlFileOutputStep() {
		return stepBuilderFactory.get("xmlFileOutputStep")
				.<Customer,Customer>chunk(1)
				.reader(dbJdbcCustomerInputReader)
				.writer(xmlCustomerOutputWriter)
				.build();
	}

}
