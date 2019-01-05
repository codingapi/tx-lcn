# 负载与集群配置

&nbsp;&nbsp;&nbsp;&nbsp;负载集群分为业务模块与TxManager

* 业务模块负载集群说明

&nbsp;&nbsp;&nbsp;&nbsp;模块的集群集群基于springcloud或dubbo机制，集群的方式都是围绕服务发现来完成的，关于模块的负载集群配置这里将不阐述，可参考dubbo与springcloud资料。

* TxManager集群说明

&nbsp;&nbsp;&nbsp;&nbsp;TxManager集群比较简单，只需要控制TxManager下的db资源相同(mysql 、redis)部署多份即可,注意TxManager负载均衡5.0版本与之前版本机制不同。


## TX-LCN 负载均衡介绍


使用步骤：

1. 首选需要启动多个TxManager服务。

2. 在客户端配置TxManager服务地址。

```
tx-lcn.client.manager-address=127.0.0.1:8070,127.0.0.1:8072
```

原理介绍：

当有事务请求客户端时事务发起端会随机选择一个可用TxManager作为事务控制方，然后告知其参与模块都与该模块通讯。

> 目前TX-LCN的负载机制仅提供了随机机制。

* 关于tx-lcn.client.manager-address的注意事项：

1. 客户端在配置上tx-lcn.client.manager-address地址后,启动时必须要全部可访问客户端才能正常启动。

2. 当tx-lcn.client.manager-address中的服务存在不可用时，客户端会重试链接11次，超过次数以后将不在重试，重试链接的间隔时间为15秒，当所有的TxManager都不可访问则会导致所有的分布式事务请求都失败回滚。

3. 当增加一个新的TxManager的集群模块时不需要添加到tx-lcn.client.manager-address下，TxManager也会广播到所有的TxManager端再通知所有链接中的TxClient端新的TxManager加入。



## 模块端负载集群注意事项

&nbsp;&nbsp;&nbsp;&nbsp;目前TX-LCN支持的事务种类有三种，其中LCN模式是会占用资源，详情见LCN模式原理。

我们需要考虑一种特殊情况，存在这样的一条业务请求链,A模块先调用了B模块的one方法，然后在调用了two方法，如下所示：

```
A ->B.one();
A ->B.two();
```
假如one与two方法的业务都是在修改同一条数据,假如两个方法的id相同，伪代码如下:

```
void one(id){
   execute => update demo set state = 1 where id = {id} ;
}

void two(id){
   execute => update demo set state = 2 where id = {id} ;
}

```


若B模块做了集群存在B1、B2两个模块。那么就可能出现A分别调用了B1 B2模块，如下:

```
A ->B1.one();
A ->B2.two();
```

在这样的情况下TX-LCN将在LCN与TXC模式下会因为资源占用而导致业务执行失败而回滚事务。为了支持这样的事情场景，TX-LCN为此做了负载的优化。

TX-LCN可控制在同一次事务下同一个被负载的模块被重复调用时将只会请求到第一次被选中的模块。

针对dubbo需要指定loadbalance为txlcn的负载方式，框架重写了dubbo的负载方式提供了对应dubbo的四种负载方式 :

```
txlcn_random=com.codingapi.tx.spi.sleuth.dubbo.loadbalance.TXLCNRandomLoadBalance
txlcn_roundrobin=com.codingapi.tx.spi.sleuth.dubbo.loadbalance.TXLCNRoundRobinLoadBalance
txlcn_leastactive=com.codingapi.tx.spi.sleuth.dubbo.loadbalance.TXLCNLeastActiveLoadBalance
txlcn_consistenthash=com.codingapi.tx.spi.sleuth.dubbo.loadbalance.TXLCNConsistentHashLoadBalance
```
使用如下:

```
   @Reference(version = "${demo.service.version}",
            application = "${dubbo.application.e}",
            retries = -1,
            registry = "${dubbo.registry.address}",
            loadbalance = "txlcn_random")
    private EDemoService eDemoService;

```

springcloud下需要在application的配置文件下增加:

```
tx-lcn.springcloud.loadbalance.enabled=true
```