package com.bjyt.springcloud.provider.dao;


import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bjyt.springcloud.entity.FundBasic;

@Mapper
public interface FundDao {
	 /**
     * 根据id查找
     * @param deptNo
     * @return
     */
	FundBasic findByFundCode(String fundCode);

    /**
     * 查询全部
     * @return
     */
    List<FundBasic> findFundAll();
    
    long updateByFundCode(@Param("FundBasic")FundBasic FundBasic);
    
    long deleteByFundCode(String fundCode);
    
    long insertByEntity(@Param("FundBasic")FundBasic FundBasic);
    
    long insertByParams(@Param("FundFullName")String FundFullName,@Param("FundSimpleName")String FundSimpleName,@Param("FundCode")String FundCode,@Param("FundType")String FundType,@Param("FundCompany")String FundCompany,@Param("EstablishmentDate")Date EstablishmentDate,@Param("FundScale")int FundScale,@Param("FundHold")int FundHold,@Param("NetPrincipalValue")int NetPrincipalValue,@Param("CurrentNetValue")int CurrentNetValue,@Param("YesterdayBenefit")int YesterdayBenefit,@Param("HoldBenefit")int HoldBenefit,@Param("HoldBenefitRate")String HoldBenefitRate,@Param("BenefitReminder")String BenefitReminder,@Param("BonusType")String BonusType,@Param("TradeRule")String TradeRule,@Param("InvestmentObjective")String InvestmentObjective,@Param("Announcement")String Announcement,@Param("DbSource")String DbSource);
}
