#
# XXL-JOB v2.4.0-SNAPSHOT
# Copyright (c) 2015-present, xuxueli.

CREATE database if NOT EXISTS `tx_lcn` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `tx_lcn`;

SET NAMES utf8mb4;

CREATE TABLE `tx_lcn_test1` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `exec_desc` varchar(255) NOT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


commit;

