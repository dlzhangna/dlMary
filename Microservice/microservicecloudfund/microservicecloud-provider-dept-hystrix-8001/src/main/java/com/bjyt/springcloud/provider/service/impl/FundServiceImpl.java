package com.bjyt.springcloud.provider.service.impl;


import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjyt.springcloud.entity.FundBasic;
import com.bjyt.springcloud.provider.dao.FundDao;
import com.bjyt.springcloud.provider.service.FundService;

@Service
public class FundServiceImpl implements FundService{
	@Autowired
	private FundDao dao;
	 /**
           * 根据id查找
     * @param deptNo
     * @return
     */
   @Override
   public FundBasic get(String fundCode) {
	   return dao.findByFundCode(fundCode);
   }
   
   @Override
   public long update(FundBasic fundBasic) {
	   return dao.updateByFundCode(fundBasic);
   }
   
   @Override
   public long delete(String fundCode) {
	   return dao.deleteByFundCode(fundCode);
   }
   
   @Override
   public long insertByParams(String FundFullName,String FundSimpleName,String FundCode,String FundType,String FundCompany,Date EstablishmentDate,int FundScale,int FundHold,int NetPrincipalValue,int CurrentNetValue,int YesterdayBenefit,int HoldBenefit,String HoldBenefitRate,String BenefitReminder,String BonusType,String TradeRule,String InvestmentObjective,String Announcement,String DbSource) {
	      long id = dao.insertByParams(FundFullName,FundSimpleName,FundCode,FundType,FundCompany,EstablishmentDate,FundScale,FundHold,NetPrincipalValue,CurrentNetValue,YesterdayBenefit,HoldBenefit,HoldBenefitRate,BenefitReminder,BonusType,TradeRule,InvestmentObjective,Announcement,DbSource);
	      System.out.println("id:" + id);
	      return id;
   }
   
   @Override
   public long insertFundByEntity(FundBasic fundBasic) {
	      dao.insertByEntity(fundBasic);
	      long id = fundBasic.getId();
	      System.out.println("id:" + id);
	      return id;
   }

    /**
     * 查询全部
     * @return
     */
   @Override
   public List<FundBasic> list(){
    	return dao.findFundAll();
    }
}
