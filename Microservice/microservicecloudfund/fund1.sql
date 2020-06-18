SET FOREIGN_KEY_CHECKS=0;
CREATE database fund1 DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use fund1;

DROP TABLE IF EXISTS `fund_basic`;
CREATE TABLE `fund_basic` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Fund_full_name` varchar(20) NOT NULL,
  `Fund_simple_name` varchar(100) NOT NULL,
  `Fund_code` varchar(100) NOT NULL,
  `Fund_type` varchar(200) NOT NULL,
  `Fund_company` varchar(200) NOT NULL,
  `Establishment_date` date NOT NULL,
  `Fund_scale` int NOT NULL,
  `Fund_hold` int NOT NULL,
  `Net_principal_value` int NOT NULL,
  `Current_Net_value` int NOT NULL,
  `Yesterday_Benefit` int NOT NULL,
  `Hold_Benefit` int NOT NULL,
  `Hold_Benefit_Rate` varchar(200) NOT NULL,
  `Benefit_Reminder` varchar(200) NOT NULL,
  `Bonus_type` varchar(200),
  `Trade_rule` varchar(200),
  `Investment_objective` varchar(200),
  `Announcement` varchar(200),
  `Db_source` varchar(200),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`fund_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

BEGIN;

INSERT INTO `fund_basic` VALUES ('1','中欧瑞丰灵活配置混合型证券投资基金A','中欧瑞丰灵活配置A','166023','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,10000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8',DATABASE());
INSERT INTO `fund_basic` VALUES ('2','中欧瑞丰灵活配置混合型证券投资基金B','中欧瑞丰灵活配置B','166024','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,20000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8',DATABASE());
INSERT INTO `fund_basic` VALUES ('3','中欧瑞丰灵活配置混合型证券投资基金C','中欧瑞丰灵活配置C','166025','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,30000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8',DATABASE());
commit;
select * from fund_basic;
INSERT INTO `fund_basic` VALUES ('1','中欧瑞丰灵活配置混合型证券投资基金A','中欧瑞丰灵活配置A','166023','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,10000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund2 DB');
INSERT INTO `fund_basic` VALUES ('2','中欧瑞丰灵活配置混合型证券投资基金B','中欧瑞丰灵活配置B','166024','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,20000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund2 DB');
INSERT INTO `fund_basic` VALUES ('3','中欧瑞丰灵活配置混合型证券投资基金C','中欧瑞丰灵活配置C','166025','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,30000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund2 DB');
select * from fund_basic;
INSERT INTO `fund_basic` VALUES ('1','中欧瑞丰灵活配置混合型证券投资基金A','中欧瑞丰灵活配置A','166023','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,10000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
INSERT INTO `fund_basic` VALUES ('2','中欧瑞丰灵活配置混合型证券投资基金B','中欧瑞丰灵活配置B','166024','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,20000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
INSERT INTO `fund_basic` VALUES ('3','中欧瑞丰灵活配置混合型证券投资基金C','中欧瑞丰灵活配置C','166025','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,30000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
select * from fund_basic;

delete from fund_basic where id in ('1','2','3');
COMMIT;
