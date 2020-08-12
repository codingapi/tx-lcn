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
DROP TABLE IF EXISTS `lcn_sql_parse_user`;
CREATE TABLE `lcn_sql_parse_user` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL comment '姓名',
  `sex` varchar(10)  DEFAULT NULL comment '性别',
  `job` varchar(32) DEFAULT NULL comment '工作',
  `home_address` varchar(128) default null comment '家庭住址',
  `age` int default null comment '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;