/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50636
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50636
 File Encoding         : 65001

 Date: 03/07/2020 13:47:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
