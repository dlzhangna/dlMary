SET FOREIGN_KEY_CHECKS=0;
create database fund3 DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use fund3;
BEGIN;
INSERT INTO `fund_basic` VALUES ('1','中欧瑞丰灵活配置混合型证券投资基金A','中欧瑞丰灵活配置A','166023','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,10000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
INSERT INTO `fund_basic` VALUES ('2','中欧瑞丰灵活配置混合型证券投资基金B','中欧瑞丰灵活配置B','166024','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,20000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
INSERT INTO `fund_basic` VALUES ('3','中欧瑞丰灵活配置混合型证券投资基金C','中欧瑞丰灵活配置C','166025','开放式(带固定封闭期)','中欧基金管理有限公司','2017-07-31',1425487790.18,30000,1,1.2438,0.02,2295.55,'22.96%','0','1','1','追求资产净值的稳定增长','2,3,5,6,8','fund3 DB');
commit;
select * from fund_basic;
delete from fund_basic where id in ('1','2','3');
ALTER table fund_basic ADD db_source varchar(200) NULL COMMENT 'DBName';
alter table fund_basic modify db_source varchar(200) DEFAULT NULL;

