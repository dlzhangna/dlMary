package com.bjyt.springcloud.writer;

import java.util.Arrays;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjyt.springcloud.entity.Customer;

@Configuration
public class CompositeItemOutputWriter {
	
	@Autowired
	public StaxEventItemWriter<Customer> xmlCustomerOutputWriter;
	
	@Autowired
	public FlatFileItemWriter<Customer> jsonCustomerOutputWriter;
	
	
    @Bean
    public CompositeItemWriter<Customer> customerCompositeItemWriter() throws Exception {
        CompositeItemWriter<Customer> itemWriter = new CompositeItemWriter<>();
        itemWriter.setDelegates(Arrays.asList(xmlCustomerOutputWriter,jsonCustomerOutputWriter));
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
