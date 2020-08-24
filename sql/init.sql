/ 
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50636
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50636
 File Encoding         : 65001

 Date: 08/08/2020 23:29:07
 /

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


create database test;
use test;

-- ----------------------------
-- Table structure for lcn_demo
-- ----------------------------
DROP TABLE IF EXISTS `lcn_demo`;
CREATE TABLE `lcn_demo` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `module` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;


--        删除语句分析测试样例
DROP TABLE IF EXISTS `lcn_sql_parse_test1`;
CREATE TABLE `lcn_sql_parse_test1` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL comment '姓名',
  `sex` varchar(10)  DEFAULT NULL comment '性别',
  `job` varchar(32) DEFAULT NULL comment '工作',
  `home_address` varchar(128) default null comment '家庭住址',
  `age` int default null comment '年龄',
   `dept_id`     int default 0 not null comment '部门编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `lcn_sql_parse_test2`;
CREATE TABLE `lcn_sql_parse_test2` (
    `id`        int(12) auto_increment
        primary key,
    `dept_name` varchar(32) null comment '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

create table lcn_test.lcn_sql_parse_test3
(
    id           varbinary(32) not null
        primary key,
    name         varchar(30)   null comment '姓名',
    sex          varchar(10)   null comment '性别',
    job          varchar(32)   null comment '工作',
    home_address varchar(128)  null comment '家庭住址',
    age          int           null comment '年龄',
    dept_id      int default 0 not null comment '部门编号'
)
    charset = utf8mb4;