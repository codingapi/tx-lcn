# 通讯协议扩展

通讯协议扩展是指txclient与txmanager通讯的协议扩展。

目前TX-LCN默认采用了netty方式通讯。关于拓展也以netty方式来说明如何拓展。


1. 拓展tx-spi-rpc

也扩展提供其他机制，重写`com.codingapi.tx.spi.rpc.loadbalance.RpcLoadBalance`