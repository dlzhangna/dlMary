package com.bjyt.springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ConfigEurekaServer1001App {
	public static void main(String[] args) {
        SpringApplication.run(ConfigEurekaServer1001App.class,args);
    }
}
