package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class FundConfig3344App {
	public static void main(String[] args) {
		  SpringApplication.run(FundConfig3344App.class, args);
	  }
}
