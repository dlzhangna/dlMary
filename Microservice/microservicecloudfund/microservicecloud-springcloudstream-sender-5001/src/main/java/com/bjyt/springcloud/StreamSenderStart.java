package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import com.bjyt.springcloud.common.ISendeService;

@SpringBootApplication
//@EnableEurekaClient
@EnableBinding(value={ISendeService.class})
public class StreamSenderStart {
	public static void main(String[] args) {
        SpringApplication.run(StreamSenderStart.class, args);
    }
}
