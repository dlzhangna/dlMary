package com.bjyt.springcloud.errorhanding;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatch6002ErrorHandingApp {
	public static void main(String[] args) {
        SpringApplication.run(SpringBatch6002ErrorHandingApp.class, args);
    }
}
