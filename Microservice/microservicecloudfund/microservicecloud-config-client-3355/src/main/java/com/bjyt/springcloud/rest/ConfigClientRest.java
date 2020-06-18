package com.bjyt.springcloud.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientRest {
	@Value("${spring.application.name}")
	private String applicationName;
	
	@Value("${eureka.client.service-url.defaultZone}")
	private String eurekaServers;
	
	@Value("${server.port}")
	private String port;

	@RequestMapping("/config")
	public String getConfig() {

//		System.out.println("applicationName:" + this.applicationName 
//				);
		System.out.println("applicationName3355: " + this.applicationName + " eurekaServers: " + this.eurekaServers + " port: "
				+ this.port);
		return "ApplicationName3355: " + this.applicationName  + "\n EurekaServers: " + this.eurekaServers + "\n Port: " + this.port;
//		return "applicationName:" + this.applicationName;
	}
}
