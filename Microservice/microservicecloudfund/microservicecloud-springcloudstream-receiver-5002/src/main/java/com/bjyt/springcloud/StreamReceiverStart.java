package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.bjyt.springcloud.common.IReceiverService;

@SpringBootApplication
//@EnableEurekaClient
@EnableBinding(value={IReceiverService.class})
public class StreamReceiverStart {
	public static void main(String[] args) {
        SpringApplication.run(StreamReceiverStart.class, args);
    }
}
