package com.bjyt.springcloud;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import com.bjyt.springcloud.writer.XmlOutputWriter;


@SpringBootApplication
@EnableBatchProcessing
//@EnableScheduling
//@ComponentScan(value = "com.bjyt.springcloud", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {XmlOutputWriter.class}))
public class SpringBatch6001App {
	public static void main(String[] args) {
        SpringApplication.run(SpringBatch6001App.class, args);
    }
}
