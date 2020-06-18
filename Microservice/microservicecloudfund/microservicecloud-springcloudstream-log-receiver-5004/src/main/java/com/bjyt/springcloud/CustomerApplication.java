package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import com.bjyt.springcloud.common.MyProcessor;

@SpringBootApplication
@EnableBinding(value = {MyProcessor.class})
public class CustomerApplication {
	public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
