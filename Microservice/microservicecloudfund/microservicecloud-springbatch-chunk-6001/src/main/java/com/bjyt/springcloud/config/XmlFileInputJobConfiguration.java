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
public class XmlFileInputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("xmlCustomerInputReader")
	private ItemReader<Customer> xmlCustomerInputReader;
	
//	@Autowired
//	@Qualifier("xmlFileCustomerWriter")
//	private ItemWriter<? super Customer> xmlFileCustomerWriter;
	
	@Autowired
	@Qualifier("xmlFileInputWriter")
	private ItemWriter<? super Customer> xmlFileInputWriter;
	

	@Bean
	public Job xmlFileInputJob() {
		return jobBuilderFactory.get("xmlFileInputJob")
			.start(xmlFileInputStep())
			.build();
	}
    
	@Bean
	public Step xmlFileInputStep() {
		return stepBuilderFactory.get("xmlFileInputStep")
				.<Customer,Customer>chunk(1)
				.reader(xmlCustomerInputReader)
				.writer(xmlFileInputWriter)
				.build();
	}

}
