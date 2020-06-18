package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class FundConsumerDashBoard9001APP {
	public static void main(String[] args) {
		  SpringApplication.run(FundConsumerDashBoard9001APP.class, args);
	  }
}
