use fund1;

CREATE TABLE `activity` (
  `id` varchar(20) NOT NULL,
  `name` varchar(10),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpm` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


insert into activity values('A1','new gift',NOW(),NOW());
insert into activity values('A2','chu xiao',NOW(),Now());