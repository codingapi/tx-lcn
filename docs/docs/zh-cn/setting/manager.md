# manager配置说明

```properties

# 事务消息主机
tx-lcn.manager.host=127.0.0.1

# 事务消息端口（默认是TxManager端口号加1）
tx-lcn.manager.port=8070

# 心跳检测时间(秒)
tx-lcn.manager.heart-time=15

# 事务并发等级（实质是处理事务消息的线程数, 最小为处理器核心数的5倍）
tx-lcn.manager.concurrent-level=0

# 后台管理密码
tx-lcn.manager.admin-key=codingapi

# 分布式事务执行总时间（ms）
tx-lcn.manager.dtx-time=30000

# 事务异常通知（任何http协议地址。未指定协议时，为TxManager提供的接口）
tx-lcn.manager.ex-url=/provider/email-to/ujued@qq.com
```

> `NOTE` TxManager所有配置均有默认配置，请按需覆盖默认配置。*特别注意* TxManager进程会监听两个端口号，一个为`TxManager端口`，另一个是`事务消息端口`。
TxClient默认连接`事务消息端口`是`8070`，所以，为保证TX-LCN基于默认配置运行良好，请设置`TxManager端口`号为`8069` 或者指定`事务消息端口`为`8070`