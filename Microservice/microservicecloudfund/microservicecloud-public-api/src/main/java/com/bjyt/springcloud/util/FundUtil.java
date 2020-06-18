package com.bjyt.springcloud.util;

import java.sql.Date;

import com.bjyt.springcloud.entity.FundBasic;

public class FundUtil {
	public static FundBasic generateFundBasicVo() {
		  String FundFullName="JAABB";
		  String FundSimpleName = "JAB";
		  String FundCode = "008972";
		  String FundType = "open";
		  String FundCompany = "Fidelity9";
		  //SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  Date EstablishmentDate = Date.valueOf("2019-09-01");
		  int FundScale =  100000;
		  int FundHold = 1000;
		  int NetPrincipalValue = 1;
		  int CurrentNetValue = 0;
		  int YesterdayBenefit = 0;
		  int HoldBenefit = 2296;
		  String HoldBenefitRate = "35%";
		  String BenefitReminder = "0";
		  String BonusType = "1";
		  String TradeRule = "1";
		  String InvestmentObjective = "Stable increase";
		  String Announcement = "1,2,4";

		  FundBasic fundBasic = new FundBasic(FundFullName,FundSimpleName,FundCode,FundType,FundCompany,EstablishmentDate,FundScale,FundHold,NetPrincipalValue,CurrentNetValue,YesterdayBenefit,HoldBenefit,HoldBenefitRate,BenefitReminder,BonusType,TradeRule,InvestmentObjective,Announcement);
		  return fundBasic;
//		  String FundFullName;
//		  String FundSimpleName;
//		  String FundCode;
//		  String FundType;
//		  String FundCompany;
//		  Date EstablishmentDate;
//		  int FundScale;
//		  int FundHold;
//		  int NetPrincipalValue;
//		  int CurrentNetValue;
//		  int YesterdayBenefit;
//		  int HoldBenefit;
//		  String HoldBenefitRate;
//		  String BenefitReminder;
//		  String BonusType;
//		  String TradeRule;
//		  String InvestmentObjective;
//		  String Announcement;
	  }

}
