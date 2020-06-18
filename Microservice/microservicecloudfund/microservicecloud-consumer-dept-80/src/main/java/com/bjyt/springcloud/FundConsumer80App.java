package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FundConsumer80App {
  public static void main(String[] args) {
	  SpringApplication.run(FundConsumer80App.class, args);
  }
}
