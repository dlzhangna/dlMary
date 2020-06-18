package com.bjyt.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import zipkin.server.EnableZipkinServer;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@SpringBootApplication
@EnableEurekaClient
//@EnableZipkinServer
@EnableZipkinStreamServer
public class ZipkinServiceApplication {
	public static void main(String[] args) {
        SpringApplication.run(ZipkinServiceApplication.class, args);
    }
}
