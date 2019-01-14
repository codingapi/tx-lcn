# tx-lcn分布式事务框架 (5.0.0.RC1)

[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![BBS](https://bbs.txlcn.org/style/Archlinux/txlcn-bbs.svg)](https://bbs.txlcn.org)

## 项目介绍
5.0完全拥抱springboot体系,JDK版本为1.8开发，将不仅仅支持LCN事务模式，也引入了TCC，TXC模式，同时把分布式事务协调的模式抽象出来，让各种模式可以嵌套使用。


版本主要特点：
1.  将基于springboot 2.0研发,将替换groupId传递机制，由sleuth机制处理。
2.  将抽离LCN封装业务，提出业务接口层与通讯层，将可支持自定义分布式事务模式与通讯模式。
3.  将支持LCN TXC TCC 三种事务模式，且可混合支持。
4.  性能继续优化，去掉线程等待机制，提高吞吐量。



## 模块划分

docs : 官网文档
tx-client:TXLCN分布式事务客户端   
tx-client-dubbo:dubbo框架客户端   
tx-client-springcloud:springcloud框架客户端   
tx-commons:公共模块   
tx-jdbcproxy-p6spy:sql拦截代理，采用了p6spy方式。  
tx-logger:性能测试日志 
tx-manager:TXLCN事务管理器   
tx-spi-message:消息扩展接口   
tx-spi-message-netty:netty消息实现  
tx-spi-sleuth:sleuth扩展接口   


