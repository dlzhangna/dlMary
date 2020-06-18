package com.bjyt.springcloud.provider.service;


import java.sql.Date;
import java.util.List;

import com.bjyt.springcloud.entity.FundBasic;



public interface FundService {
	 FundBasic get(String fundCode);
	 List<FundBasic> list();
	 long update(FundBasic fundBasic);
	 long delete(String fundCode);
	 long insertFundByEntity(FundBasic fundBasic);
	 long insertByParams(String FundFullName,String FundSimpleName,String FundCode,String FundType,String FundCompany,Date EstablishmentDate,int FundScale,int FundHold,int NetPrincipalValue,int CurrentNetValue,int YesterdayBenefit,int HoldBenefit,String HoldBenefitRate,String BenefitReminder,String BonusType,String TradeRule,String InvestmentObjective,String Announcement,String DbSource);
}
