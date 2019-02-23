# TX-LCN分布式事务框架 (5.0.2.RELEASE)

[![Maven](https://img.shields.io/badge/endpoint.svg?url=https://bbs.txlcn.org/maven-central)](https://bbs.txlcn.org/maven-list)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/codingapi/tx-lcn/blob/master/LICENSE)
[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


## 项目介绍

5.0完全拥抱springboot体系，基于JDK1.8，不仅仅支持LCN事务模式，同时引入了TCC，TXC模式，把分布式事务协调的模式抽象出来，让各种模式可以嵌套使用。

事务模式说明：TCC是用户自己实现提交回滚业务，LCN是框架代理JDBC Connection实现提交回滚业务, TXC是框架分析业务SQL提前提交，在需要回滚时生成逆向回滚。

TCC忽略不说。LCN与TXC两种框架实现的事务模式到底那个更优越呢？

理论上，在并发量大，资源相对紧张时，TXC要优于LCN，但从目前测试上来说，结果却非如此，LCN还是稍优于TXC。同时TXC还限制了业务SQL形式，未做到完全逆向
后续版本将会着重优化TXC，包括性能上和SQL的完全逆向上。


版本较4.x主要特点：
1. 基于SpringBoot研发
2. 抽离LCN封装业务，提出业务接口层与通讯层，可支持自定义分布式事务模式与通讯模式。
3. 支持LCN TXC TCC 三种事务模式，且可混合支持。
4. 性能较优秀，去掉了线程等待机制，提高吞吐量。



## 模块划分

1. txlcn-tc:*TXLCN分布式事务客户端*
2. txlcn-common:*公共模块*   
3. txlcn-logger:*日志模块。(默认提供日志持久化到MySQL的支持)* 
4. txlcn-tm:*TXLCN事务管理器*   
5. txlcn-txmsg:*事务消息扩展接口*   
6. txlcn-txmsg-netty:*事务消息接口的Netty实现*  
7. txlcn-tracing:*分布式事务追踪工具*

## 官网文档

官网:https://www.txlcn.org    
统计:[留下您的公司信息](https://github.com/codingapi/tx-lcn/issues/7)    

技术交流群：554855843(已满) 970071379(未满)

