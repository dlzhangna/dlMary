package com.bjyt.springcloud.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.bjyt.springcloud.entity.Customer;


@Configuration
public class MultipleFileInputJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Value("classpath*:/file*.csv")
	private Resource[] inputFiles;
	
	@Autowired
	@Qualifier("flatFileCustomerInputReader")
	private FlatFileItemReader<Customer> flatFileCustomerInputReader;
	
	@Autowired
	@Qualifier("customerWriter")
	private ItemWriter<? super Customer> customerWriter;

	@Bean
	public Job multipleFileInputJob() {
		return jobBuilderFactory.get("multipleFileInputJob")
			.start(multipleFlatFileInputStep())
			.build();
	}
    
	@Bean
	public Step multipleFlatFileInputStep() {
		return stepBuilderFactory.get("multipleFlatFileInputStep")
				.<Customer,Customer>chunk(100)
				.reader(MultiResourceReader())
				.writer(customerWriter)
				.build();
	}
	
	@Bean
	@StepScope
	public MultiResourceItemReader<Customer> MultiResourceReader() {
		
		MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();
		
		reader.setDelegate(flatFileCustomerInputReader);
		reader.setResources(inputFiles);
		
		return reader;
	}

}
