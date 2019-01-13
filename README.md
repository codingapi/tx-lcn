# tx-lcn分布式事务框架 (5.0.0.RC1)
[![Gitter](https://badges.gitter.im/codingapi/tx-lcn.svg)](https://gitter.im/codingapi/tx-lcn?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
## 项目介绍
5.0完全拥抱springboot体系,JDK版本为1.8开发，将不仅仅支持LCN事务模式，也引入了TCC，TXC模式，同时把分布式事务协调的模式抽象出来，让各种模式可以嵌套使用。


版本主要特点：
1.  将基于springboot 2.0研发,将替换groupId传递机制，由sleuth机制处理。
2.  将抽离LCN封装业务，提出业务接口层与通讯层，将可支持自定义分布式事务模式与通讯模式。
3.  将支持LCN TXC TCC 三种事务模式，且可混合支持。
4.  性能继续优化，去掉线程等待机制，提高吞吐量。



## 模块划分

docs : 官网文档
example : 测试demo   
tx-client:TXLCN分布式事务客户端   
tx-client-dubbo:dubbo框架客户端   
tx-client-springcloud:springcloud框架客户端   
tx-commons:公共模块   
tx-jdbcproxy-p6spy:sql拦截代理，采用了p6spy方式。  
tx-logger:性能测试日志 
tx-manager:TXLCN事务管理器   
tx-spi-rpc:rpc扩展接口   
tx-spi-rpc-netty:rpc netty实现  
tx-spi-sleuth:sleuth扩展接口   



## 运行说明

需要准备的服务：

* redis       用于tx-manager事务数据储存。
* mysql       demo操作数据,事务日志数据记录，TxManager异常数据。
* zookeeper   [dubbo] demo依赖
* consul      [springcloud] demo依赖



数据的初始化：

1. 将example下的 txlcn-demo.sql 导入到txlcn-demo数据库。
2. 将tx-manager下的 tx-manager.sql 导入到tx-manager数据库。


demo 调用链说明:

demo-client LCN事务，事务发起方，调用了d与e   
demo-d TXC事务，事务参与方   
demo-e TCC事务，事务参与方   


启动顺序：

1. 启动TxManager。   
2. 启动example下的三个demo。   


效果说明:

访问 http://ip:port/txlcn?value=xxxx,   正常响应  ok-d > ok-e > ok-client


## 当前阶段状况

1. 目前项目功能已经研发完毕，完成了自测与压测（压测并发到2000），测试报表待完善。
2. 文档尚未开始编写，后面将编写详细文档，待完善。
3. 原理说明，使用说明书，问题排查都会在官网上说明体现，待完善。
4. 尚未发布正式版，大家可以反馈好的意见，我们在发布版本之前还会不断优化。
5. 针对与TXC模式目前只是测试了简单的sql语句，对于过于复杂的更新sql可能存在问题，将会继续优化。
6. 项目将会在正式发布前继续做调整。


若发现好的意见或问题请提交到issue,感谢.
