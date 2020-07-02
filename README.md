# Distributed Transaction Framework - LCN (6.0.0)

[![Maven](https://img.shields.io/badge/endpoint.svg?url=https://bbs.txlcn.org/maven-central)](https://bbs.txlcn.org/maven-list)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/codingapi/tx-lcn/blob/master/LICENSE)
[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![codecov](https://codecov.io/gh/codingapi/tx-lcn/branch/dev6.0/graph/badge.svg)](https://codecov.io/gh/codingapi/tx-lcn)
[![Build Status](https://travis-ci.org/codingapi/tx-lcn.svg?branch=dev6.0)](https://travis-ci.org/codingapi/tx-lcn)

## 参与方式
* 了解原理 [LCN分布式事务框架](LCN分布式事务框架-20200102.pdf)
* 了解代码
* 参与任务 [issues](https://github.com/codingapi/tx-lcn/issues) 

## 代码提交步骤
* fork该项目地址,并更新代码
* 认领任务或发布问题
* 维护代码编写测试
* 发起合并请求,关联issues
* 代码审核通过合并到仓库中 
  
## 代码结构
* example:                  *示例与测试相关的代码*    
* starter-txlcn-protocol:   *txlcn-protocol模块的starter*   
* starter-txlcn-tc:        *txlcn-tc模块的starter* 
* txlcn-p6spy:             *p6spy-解析sql与jdbc的event定义*
* txlcn-protocol:          *通讯协议制度* 
* txlcn-tc:                 *TC事务客户端模块* 
* txlcn-tm:                 *TM事务控制端* 

## 从0到1实现分布式事务 公开课

### 第一节课     
分布式事务从0到1-认识分布式事务    
[原文](https://www.codingapi.com/docs/txlcn-lesson01/) [B站](https://www.bilibili.com/video/av80626430/)  
### 第二节课
分布式事务从0到1-了解TX-LCN原理    
[原文](https://www.codingapi.com/docs/txlcn-lesson02/)  [B站-原理一](https://www.bilibili.com/video/av80676649)  [B站-原理二](https://www.bilibili.com/video/av80676836)

B站地址   
https://space.bilibili.com/386239614  
公众号(通过公众号加群):    
![CODINGAPI分享者](qrcode.jpg)

