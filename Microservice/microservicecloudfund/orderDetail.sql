use fund1;

CREATE TABLE `orderdetail` (
  `order_id` bigint(20) NOT NULL,
  `category_id` bigint(10),
  `sku` bigint(20),
  `money` bigint(20),
  `amount` bigint(10),
  `create_time` date,
  `update_time` date,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(5001,1,10001,500,2,NOW(),NOW());
insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(5001,2,20001,1000,1,NOW(),NOW());
insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(28001,1,10001,500,2,NOW(),NOW());
insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(28001,2,20001,1000,1,NOW(),NOW());
insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(29001,1,10001,500,2,NOW(),NOW());
insert into orderdetail(order_id,category_id,sku,money,amount,create_time,update_time)values(29001,2,20001,1000,1,NOW(),NOW());