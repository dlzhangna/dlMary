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

u001,A1,2019-09-02 10:10:11,1,"BeiJing"
u002,A1,2019-09-02 10:11:11,1,"LiaoNing"
u001,A1,2019-09-02 10:11:11,2,"BeiJing"
u001,A1,2019-09-02 10:11:30,3,"BeiJing"
u002,A1,2019-09-02 10:12:11,2,"LiaoNing"
u003,A2,2019-09-02 10:13:11,1,"ShanDong"
u003,A2,2019-09-02 10:13:20,2,"ShanDong"
u003,A2,2019-09-02 10:14:20,3,"ShanDong"
u004,A1,2019-09-02 10:15:20,1,"BeiJing"
u004,A1,2019-09-02 10:15:20,2,"BeiJing"
u005,A1,2019-09-02 10:15:20,1,"HeBei"
u001,A2,2019-09-02 10:16:11,1,"BeiJing"
u001,A2,2019-09-02 10:16:11,2,"BeiJing"
u002,A1,2019-09-02 10:16:11,2,"LiaoNing"
u002,A1,2019-09-02 10:16:11,3,"LiaoNing"