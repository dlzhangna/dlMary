package com.bjyt.springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer7003App {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer7003App.class,args);

	}
}
