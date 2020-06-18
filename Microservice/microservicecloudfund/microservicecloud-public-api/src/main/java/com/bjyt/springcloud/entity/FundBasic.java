package com.bjyt.springcloud.entity;

import java.io.Serializable;
import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
//import lombok.AllArgsConstructor;



//@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain=true)
@SuppressWarnings("serial")
public class FundBasic implements Serializable {

	//private static final long serialVersionUID = -1427542702625741372L;
	public Long Id;
	public String FundFullName;
	public String FundSimpleName;
	public String FundCode;
	public String FundType;
	public String FundCompany;
	public Date EstablishmentDate;
	public int FundScale;
	public int FundHold;
	public int NetPrincipalValue;
	public int CurrentNetValue;
	public int YesterdayBenefit;
	public int HoldBenefit;
	public String HoldBenefitRate;
	public String BenefitReminder;
	public String BonusType;
	public String TradeRule;
	public String InvestmentObjective;
	public String Announcement;
	public String DbSource;
	
	public FundBasic(String fundFullName, String fundSimpleName, String fundCode, String fundType, String fundCompany,
			Date establishmentDate, int fundScale, int fundHold, int netPrincipalValue, int currentNetValue,
			int yesterdayBenefit, int holdBenefit, String holdBenefitRate, String benefitReminder, String bonusType,
			String tradeRule, String investmentObjective, String announcement) {
		super();
		FundFullName = fundFullName;
		FundSimpleName = fundSimpleName;
		FundCode = fundCode;
		FundType = fundType;
		FundCompany = fundCompany;
		EstablishmentDate = establishmentDate;
		FundScale = fundScale;
		FundHold = fundHold;
		NetPrincipalValue = netPrincipalValue;
		CurrentNetValue = currentNetValue;
		YesterdayBenefit = yesterdayBenefit;
		HoldBenefit = holdBenefit;
		HoldBenefitRate = holdBenefitRate;
		BenefitReminder = benefitReminder;
		BonusType = bonusType;
		TradeRule = tradeRule;
		InvestmentObjective = investmentObjective;
		Announcement = announcement;
	}
    
	
}
