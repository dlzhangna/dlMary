package com.bjyt.springcloud.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bjyt.springcloud.entity.FundBasic;
import com.bjyt.springcloud.util.FundUtil;

@RestController
public class FundController_Consumer {
	
	//private static final String REST_URL_PREFIX = "http://localhost:8001";
	private static final String REST_URL_PREFIX = "http://MICROSERVICE-PROVIDER-FUND";
	
    @Autowired
	private RestTemplate restTemplate;
    
    @RequestMapping(value = "/consumer/fund/insert")
    public long insert() {
    	List<FundBasic> fundBasicList = new ArrayList<FundBasic>();
    	FundBasic fundBasic = FundUtil.generateFundBasicVo();
    	fundBasicList.add(fundBasic);
    	System.out.println("fundCode in consumer:" + fundBasic.getFundCode());
    	return restTemplate.postForObject(REST_URL_PREFIX + "/fund/insert", fundBasicList, long.class);
    }
    @RequestMapping(value = "/consumer/fund/delete/{fundcode}")
    public long delete(@PathVariable("fundcode") String fundCode) {
    	return restTemplate.postForObject(REST_URL_PREFIX + "/fund/delete/" + fundCode, fundCode,long.class);
    }
    
    @RequestMapping(value= "/consumer/fund/updateByFundCode/{fundcode}")
    public long update(@PathVariable("fundcode") String fundCode) {
    	return restTemplate.postForObject(REST_URL_PREFIX + "/fund/updateByFundCode/" + fundCode,fundCode,long.class);
    }
    
    @RequestMapping(value= "/consumer/fund/updateByEntity")
    public long update() {
    	FundBasic fundBasic = FundUtil.generateFundBasicVo();
    	System.out.println("fundCode in consumer:" + fundBasic.getFundCode());
    	return restTemplate.postForObject(REST_URL_PREFIX + "/fund/updateByEntity",fundBasic,long.class);
    }
    @RequestMapping(value= "/consumer/fund/get/{fundcode}")
    public FundBasic get(@PathVariable("fundcode") String fundCode) {
    	return restTemplate.getForObject(REST_URL_PREFIX + "/fund/get/" + fundCode,FundBasic.class);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value= "/consumer/fund/list")
    public List<FundBasic> getAll() {
    	return restTemplate.getForObject(REST_URL_PREFIX + "/fund/list",List.class);
    }
    @RequestMapping(value = "/consumer/fund/discovery")
    public Object discovery() {
    	return restTemplate.getForObject(REST_URL_PREFIX + "/fund/discovery", Object.class);
    }
}
