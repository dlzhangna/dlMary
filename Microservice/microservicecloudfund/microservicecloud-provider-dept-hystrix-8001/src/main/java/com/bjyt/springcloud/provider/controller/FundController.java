package com.bjyt.springcloud.provider.controller;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bjyt.springcloud.entity.FundBasic;
import com.bjyt.springcloud.provider.service.FundService;
import com.bjyt.springcloud.util.FundUtil;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class FundController {
  @Autowired
  private FundService fundService;
  
  @Autowired
  private DiscoveryClient discoveryClient;
  
  @RequestMapping(value = "/fund/discovery")
  public Object discovery() {
	   List<String> list = discoveryClient.getServices();
	   System.out.println("**************" + list);
	   List<ServiceInstance> serverList = discoveryClient.getInstances("MICROSERVICECLOUD-FUND");
       for(ServiceInstance element : serverList) {
    	   System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
       }
       return this.discoveryClient;
  }
   
  @RequestMapping(value="/fund/get/{fundcode}",method=RequestMethod.GET)
  //@HystrixCommand(fallbackMethod = "processHystrix_Get")
  public FundBasic get(@PathVariable("fundcode") String fundCode) {
	  System.out.println("get method in hystrix-8001 start");
	  FundBasic fundBasic = this.fundService.get(fundCode);
	  System.out.println("fundBasic：" + fundBasic);
	  System.out.println(null==fundBasic);
	  if(null != fundBasic) {
	    System.out.println("fundCode:" + fundBasic.getFundCode());
	  }
	  if(null==fundBasic.getFundCode() || "".equals(fundBasic.getFundCode())) {
		  System.out.println("if condition start");
		  throw new RuntimeException("This fundcode:" + fundCode + "don't match any records");
	  }
	  System.out.println("get method in hystrix-8001 end");
	  return fundBasic;
  }
  
  public FundBasic processHystrix_Get(@PathVariable("fundcode") String fundcode) {
	  System.out.println("processHystrix_Get method");
	  return new FundBasic().setFundCode(fundcode).setDbSource("No this record in MySQL DB");
  }
  
  @GetMapping("/fund/list")
  public List<FundBasic> getAll() {
	  return fundService.list();
  }
  
  @RequestMapping(value= "/fund/updateByFundCode/{fundcode}")
  public long updateByFundCode(@PathVariable("fundcode") String fundCode) {	  
	  System.out.println("fundCode:" + fundCode);
	  FundBasic fundBasic = FundUtil.generateFundBasicVo();
	  fundBasic.setFundCode(fundCode);
	  long updateNum = fundService.update(fundBasic);
	  System.out.println("update records number is: " + updateNum);
	  return updateNum;
  }
  
  @RequestMapping(value= "/fund/updateByEntity")
  public long updateByEntity(@RequestBody FundBasic fundBasic) {
	  System.out.println("fundCode in FundController:" + fundBasic.getFundCode());
	  long updateNum = fundService.update(fundBasic);
	  System.out.println("update records number is: " + updateNum);
	  return updateNum;
  }
  
  @RequestMapping(value="/fund/delete/{fundcode}")
  public long delete(@PathVariable("fundcode") String fundCode) {
	  long deleteNum = fundService.delete(fundCode);
	  System.out.println("delete records number is: " + deleteNum);
	  return deleteNum;
  }
  
  @RequestMapping(value="/fund/insertOne")
  public long insert() {
	  List<FundBasic> insertList = new ArrayList<FundBasic>();
	  FundBasic fundBasic = FundUtil.generateFundBasicVo();
	  long insertKey = 0;	  
	  insertList.add(fundBasic);
	  for(FundBasic fundBasicItem : insertList){
		  Long id = fundService.insertFundByEntity(fundBasicItem);
		  //Long id = fundService.insert(FundFullName,FundSimpleName,FundCode,FundType,FundCompany,EstablishmentDate,FundScale,FundHold,NetPrincipalValue,CurrentNetValue,YesterdayBenefit,HoldBenefit,HoldBenefitRate,BenefitReminder,BonusType,TradeRule,InvestmentObjective,Announcement,"fund");
		  insertKey = Long.valueOf(id);
		  System.out.println("insert records number is: " + insertList.size() + "key value is: " + insertKey);
	  }
	  return insertKey;
  }
  
  @RequestMapping(value="/fund/insert")
  public long insert(@RequestBody List<FundBasic> fundBasicList) {
	  long insertKey = 0;	  
	  for(FundBasic fundBasicItem : fundBasicList){
		  Long id = fundService.insertFundByEntity(fundBasicItem);
		  insertKey = Long.valueOf(id);
		  System.out.println("insert records number is: " + fundBasicList.size() + ",key(Id) value is: " + insertKey);
	  }
	  return insertKey;
  }

}
