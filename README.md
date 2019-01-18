# tx-lcn分布式事务框架 (5.0.0.RC1)

[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![BBS](https://bbs.txlcn.org/style/Archlinux/txlcn-bbs.svg)](https://bbs.txlcn.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/codingapi/tx-lcn/blob/master/LICENSE)


## 项目介绍

5.0完全拥抱springboot体系,JDK版本为1.8开发，将不仅仅支持LCN事务模式，也引入了TCC，TXC模式，同时把分布式事务协调的模式抽象出来，让各种模式可以嵌套使用。


版本主要特点：
1.  将基于springboot 2.0研发,将替换groupId传递机制，由sleuth机制处理。
2.  将抽离LCN封装业务，提出业务接口层与通讯层，将可支持自定义分布式事务模式与通讯模式。
3.  将支持LCN TXC TCC 三种事务模式，且可混合支持。
4.  性能继续优化，去掉线程等待机制，提高吞吐量。



## 模块划分

1. tx-client:*TXLCN分布式事务客户端*
2. tx-client-dubbo:*dubbo框架客户端*   
3. tx-client-springcloud:*springcloud框架客户端*   
4. tx-commons:*公共模块*   
5. tx-jdbcproxy-p6spy:*sql拦截代理，采用了p6spy方式*  
6. tx-logger:*性能测试日志* 
7. tx-manager:*TXLCN事务管理器*   
8. tx-spi-message:*消息扩展接口*   
9. tx-spi-message-netty:*netty消息实现*  
10. tx-spi-sleuth:*sleuth扩展接口*


## 官网文档

官网文档 见docs分支

https://txlcn.org


技术交流群：554855843(已满) 970071379(未满)

