package com.bjyt.springcloud.writer;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjyt.springcloud.entity.Customer;

@Configuration
public class ClassifierCompositeItemOutputWriter {
	
	@Autowired
	public StaxEventItemWriter<Customer> xmlCustomerOutputWriter;
	
	@Autowired
    public FlatFileItemWriter<Customer> jsonCustomerOutputWriter;
	
	@Bean
    public ClassifierCompositeItemWriter<Customer> customerClassifierCompositeItemWriter() throws Exception {
        ClassifierCompositeItemWriter<Customer> itemWriter = new ClassifierCompositeItemWriter<>();
        itemWriter.setClassifier(new CustomerClassifier(xmlCustomerOutputWriter,jsonCustomerOutputWriter));
        return itemWriter;
    }
}
