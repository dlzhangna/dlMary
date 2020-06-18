package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.bjyt.springcloud"})
@ComponentScan({"com.bjyt.springcloud"})
public class FundConsumer80FeignApp {
  public static void main(String[] args) {
	  SpringApplication.run(FundConsumer80FeignApp.class, args);
  }
}
