package com.bjyt.springcloud.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.bjyt.springcloud.entity.FundBasic;
import feign.hystrix.FallbackFactory;

//@Configuration
@Component
public class FundBasicClientServiceFallbackFactory implements FallbackFactory<FundBasicClientService>{
    @Override
	public FundBasicClientService create(Throwable cause) {
    	System.out.println("FundBasicClientServiceFallbackFactory create motod start");
       return new FundBasicClientService() {
    	   public FundBasic get(String fundCode) {
    		   System.out.println("get method in FundBasicClientServiceFallbackFactory");
    		   return new FundBasic().setFundCode(fundCode).setDbSource("No this record for getting in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
    	   }
    	   public long update(FundBasic fundBasic) {
    		   new FundBasic().setFundCode(fundBasic.getFundCode()).setDbSource("No this record for updating in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
    		   return 0;
    	   }
    	   public long delete(String fundCode) {
    		   new FundBasic().setFundCode(fundCode).setDbSource("No this record for deleting in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
    		   return 0;
    	   }
    	   public long insertFundByEntity(FundBasic fundBasic) {
    		   new FundBasic().setFundCode(fundBasic.getFundCode()).setDbSource("No this record for insertFundByEntity in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
    		   return 0;
    	   }
//    	   public long insertByParams(String FundFullName,String FundSimpleName,String FundCode,String FundType,String FundCompany,Date EstablishmentDate,int FundScale,int FundHold,int NetPrincipalValue,int CurrentNetValue,int YesterdayBenefit,int HoldBenefit,String HoldBenefitRate,String BenefitReminder,String BonusType,String TradeRule,String InvestmentObjective,String Announcement,String DbSource) {
//    		   new FundBasic().setFundCode(FundCode).setDbSource("No this record for insertByParams in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
//    		   return 0;
//    	   }
    	   public List<FundBasic> list(){
    		   new FundBasic().setDbSource("No this record for list in MySQL DB,Consumer will provide downgrade information,currently, the provider will be closed");
    		   return null;
    	   }
       };
    }
}
