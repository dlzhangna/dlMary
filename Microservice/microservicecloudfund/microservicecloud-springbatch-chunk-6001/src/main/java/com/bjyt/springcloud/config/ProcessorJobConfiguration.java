package com.bjyt.springcloud.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.entity.Customer;

@Configuration
public class ProcessorJobConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcCustomerInputReader")
	private JdbcPagingItemReader<Customer> dbJdbcCustomerInputReader;
	
	@Autowired
	private ItemProcessor<Customer,Customer> firstNameUpperCaseProcessor;
	
	@Autowired
    private ItemProcessor<Customer, Customer> idFilterProcessor;
	
	@Autowired
	@Qualifier("flatFileCustomerOutputWriter")
	private ItemWriter<? super Customer> flatFileCustomerOutputWriter;
	
	 @Bean
	 public Job processorCustomerJob() throws Exception {
	     return jobBuilderFactory.get("processorCustomerJob")
	            .start(processorCustomerStep())
	            .build();
	    }
	 
	 @Bean
	 public Step processorCustomerStep() throws Exception {
	      return stepBuilderFactory.get("processorCustomerStep")
	                .<Customer,Customer>chunk(100)
	                .reader(dbJdbcCustomerInputReader)
	                .processor(compositeItemProcessor())
	                .writer(flatFileCustomerOutputWriter)
	                .build();
	    }

	    @Bean
	    public CompositeItemProcessor<Customer,Customer> compositeItemProcessor(){
	        CompositeItemProcessor<Customer,Customer> processor = new CompositeItemProcessor<>();

	        List<ItemProcessor<Customer,Customer>> list = new ArrayList<>();
	        list.add(firstNameUpperCaseProcessor);
	        list.add(idFilterProcessor);
	        processor.setDelegates(list);
	        
	        return processor;
	    }
	
}
