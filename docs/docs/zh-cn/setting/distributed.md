# 负载与集群配置

## 对于微服务集群负载时注意事项
TX-LCN重写了Dubbo和SpringCloud体系下的负载均衡策略，一个分布式事务会话下，所有rpc调用在第一次时按默认的负载策略，后续rpc均会选中上一次的服务。

## TxManager的集群配置
TxManager进程监听两个端口，`TxManager端口` 和 `事务消息端口`，所以在做TxManager集群时请小心端口号！默认存在一种机制， `TxManager端口` 加1是 `事务消息端口`
。所以， 集群时只更改`TxManager端口` 就得以实现。

## TxClient对TxManager的负载
现在TxManager集群搭建完毕，来配置TxClient对TxManager的负载。把TxManager集群全部指定到TxClient` tx-lcn.client.manager-address` 配置项即可。

> `NOTE` 那TxClient是如何实现对TxManager的负载的呢?默认是对TxManager集群轮询

