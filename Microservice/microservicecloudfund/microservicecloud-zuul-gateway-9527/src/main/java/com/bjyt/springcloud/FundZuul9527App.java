package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class FundZuul9527App {
	public static void main(String[] args) {
		  SpringApplication.run(FundZuul9527App.class, args);
	  }
}
