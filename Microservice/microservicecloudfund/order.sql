use fund1;

CREATE TABLE `ordermain` (
  `oid` bigint(20) NOT NULL,
  `create_time` date,
  `total_money` bigint(50),
  `status` varchar(10),
  `update_time` date,
  `uid` bigint(20),
  `province` varchar(100),
  PRIMARY KEY (`oid`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpw` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


insert into ordermain(oid,create_time,total_money,status,update_time,uid,province) values(5001,NOW(),2000.0,1,NOW(),8888,'Beijing');
insert into ordermain(oid,create_time,total_money,status,update_time,uid,province) values(28001,NOW(),2000.0,1,NOW(),9999,'Beijing');
insert into ordermain(oid,create_time,total_money,status,update_time,uid,province) values(29001,NOW(),2000.0,1,NOW(),9999,'Beijing');