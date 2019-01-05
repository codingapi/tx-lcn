# 负载与集群配置

## 对于微服务集群负载时注意事项
TX-LCN重写了Dubbo和SpringCloud体系下的负载均衡策略，一个分布式事务会话下，所有rpc调用在第一次时按默认的负载策略，后续rpc均会选中上一次的服务。

## TxManager的集群配置
TxManager进程监听两个端口，`TxManager端口` 和 `事务消息端口`，所以在做TxManager集群时请注意端口号问题，默认有种机制是 `TxManager端口` 加1是 `事务消息端口`
，所以， 集群时注意`加1`机制，只更改`TxManager端口`就得以实现。

## TxClient对TxManager的负载
TxManager集群搭建完毕，TxClient配置`tx-lcn.client.manager-address`项，把TxManager集群全部指定即可。

> `NOTE` 那TxClient是如何实现对TxManager的负载的呢?默认是对TxManager集群轮询

