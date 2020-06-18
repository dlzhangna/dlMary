package com.bjyt.springcloud.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bjyt.springcloud.entity.FundBasic;
import com.bjyt.springcloud.service.FundBasicClientService;
import com.bjyt.springcloud.util.FundUtil;



@RestController
public class FundController_Consumer {
    @Autowired
	public FundBasicClientService fundBasicClientService;
    
    @RequestMapping(value = "/consumer/fund/insert")
    public long insert() {
    	List<FundBasic> fundBasicList = new ArrayList<FundBasic>();
    	FundBasic fundBasic = FundUtil.generateFundBasicVo();
    	fundBasicList.add(fundBasic);
    	System.out.println("fundCode in consumer:" + fundBasic.getFundCode());
    	return this.fundBasicClientService.insertFundByEntity(fundBasic);
    }
    @RequestMapping(value = "/consumer/fund/delete/{fundcode}")
    public long delete(@PathVariable("fundcode") String fundCode) {
    	return this.fundBasicClientService.delete(fundCode);
    }
    
    @RequestMapping(value= "/consumer/fund/updateByFundCode/{fundcode}")
    public long update(@PathVariable("fundcode") String fundCode) {
    	System.out.println("fundCode:" + fundCode);
  	    FundBasic fundBasic = FundUtil.generateFundBasicVo();
  	    fundBasic.setFundCode(fundCode);
  	    long updateNum = this.fundBasicClientService.update(fundBasic);
  	    System.out.println("update records number is: " + updateNum);
  	    return updateNum;
    }
    
    @RequestMapping(value= "/consumer/fund/updateByEntity")
    public long update() {
    	FundBasic fundBasic = FundUtil.generateFundBasicVo();
    	System.out.println("fundCode in consumer:" + fundBasic.getFundCode());
    	return this.fundBasicClientService.update(fundBasic);
    }
    @RequestMapping(value= "/consumer/fund/get/{fundcode}",method=RequestMethod.GET)
    public FundBasic get(@PathVariable("fundcode") String fundCode) {
    	System.out.println("get method in feign consumer start");
    	System.out.println("fundCode:" + fundCode);
    	return this.fundBasicClientService.get(fundCode);
    }
	@RequestMapping(value= "/consumer/fund/list")
    public List<FundBasic> getAll() {
    	return this.fundBasicClientService.list();
    }
}
