# tx-lcn分布式事务框架 (5.0.0.RC2)

[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![BBS](https://bbs.txlcn.org/style/Archlinux/txlcn-bbs.svg)](https://bbs.txlcn.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/codingapi/tx-lcn/blob/master/LICENSE)


## 项目介绍

5.0完全拥抱springboot体系，基于JDK1.8，不仅仅支持LCN事务模式，同时引入了TCC，TXC模式，把分布式事务协调的模式抽象出来，让各种模式可以嵌套使用。

事务模式说明：TCC是用户自己实现提交回滚业务，LCN是框架代理JDBC Connection实现提交回滚业务, TXC是框架分析业务SQL提前提交，在需要回滚时生成逆向回滚。

TCC忽略不说。LCN与TXC两种框架实现的事务模式到底那个更优越呢？在并发量大，资源相对紧张时，理论上TXC要优于LCN，
但从目前测试上来说，恰恰相反，LCN却稍微优于TXC，同时TXC还限制了业务SQL形式，未做到完全逆向。后续版本将会着重优化TXC，包括性能上和SQL的完全逆向上。


版本主要特点：
1.  将基于springboot 2.0研发
2.  将抽离LCN封装业务，提出业务接口层与通讯层，将可支持自定义分布式事务模式与通讯模式。
3.  将支持LCN TXC TCC 三种事务模式，且可混合支持。
4.  性能继续优化，去掉线程等待机制，提高吞吐量。



## 模块划分

1. txlcn-tc:*TXLCN分布式事务客户端*
2. txlcn-common:*公共模块*   
3. txlcn-logger:*性能测试日志* 
4. txlcn-tm:*TXLCN事务管理器*   
5. txlcn-txmsg:*消息扩展接口*   
6. txlcn-txmsg-netty:*Netty消息实现*  
7. txlcn-tracing:*分布式事务追踪工具*

## 官网文档

官网文档 见docs分支

https://txlcn.org


技术交流群：554855843(已满) 970071379(未满)

