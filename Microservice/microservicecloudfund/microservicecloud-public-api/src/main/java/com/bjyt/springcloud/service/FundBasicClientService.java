package com.bjyt.springcloud.service;

import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bjyt.springcloud.entity.FundBasic;

/*Create a new class which is implemented FallbackFactory-DeptClientServiceFallbackFactory
 * 
 */
//@FeignClient(value="MICROSERVICECLOUD-FUND")
@FeignClient(value="MICROSERVICE-PROVIDER-FUND",fallbackFactory=FundBasicClientServiceFallbackFactory.class)
//@FeignClient(value = "MICROSERVICECLOUD-FUND", url = "http://localhost:8001")
public interface FundBasicClientService {
	@RequestMapping(value="/fund/get/{fundcode}",method=RequestMethod.GET)
	FundBasic get(@PathVariable("fundcode") String fundCode);
	@GetMapping("/fund/list")
	List<FundBasic> list();
	@RequestMapping(value= "/fund/updateByEntity")
	long update(@PathVariable("fundBasic") FundBasic fundBasic);
	@RequestMapping(value="/fund/delete/{fundcode}")
	long delete(@PathVariable("fundCode")String fundCode);
	@RequestMapping(value="/fund/insert")
	long insertFundByEntity(@PathVariable("fundBasic") FundBasic fundBasic);
	//@RequestMapping(value="/fund/insertOne")
	//long insertByParams(String FundFullName,String FundSimpleName,String FundCode,String FundType,String FundCompany,Date EstablishmentDate,int FundScale,int FundHold,int NetPrincipalValue,int CurrentNetValue,int YesterdayBenefit,int HoldBenefit,String HoldBenefitRate,String BenefitReminder,String BonusType,String TradeRule,String InvestmentObjective,String Announcement,String DbSource);
}
