package com.bjyt.springcloud.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.entity.Customer;


@Configuration
public class MultipleFileClassifierCompositeOutputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private ItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	@Qualifier("customerClassifierCompositeItemWriter")
	private ItemWriter<? super Customer> customerClassifierCompositeItemWriter;
	
	@Autowired
	public StaxEventItemWriter<Customer> xmlCustomerOutputWriter;
	
	@Autowired
    public FlatFileItemWriter<Customer> jsonCustomerOutputWriter;

	@Bean
	public Job multipleFileClassifierOutputJob() {
		return jobBuilderFactory.get("multipleFileClassifierOutputJob")
			.start(multipleFileClassifierOutputStep())
			.build();
	}
    
	@Bean
	//@StepScope
	public Step multipleFileClassifierOutputStep() {
		return stepBuilderFactory.get("multipleFileClassifierOutputStep")
				.<Customer,Customer>chunk(5)
				.reader(dbJdbcCustomerInputReader)
				.writer(customerClassifierCompositeItemWriter)
				.stream(xmlCustomerOutputWriter)
				.stream(jsonCustomerOutputWriter)
				.build();
	}

}
