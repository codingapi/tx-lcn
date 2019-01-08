# manager配置说明

## application.properties
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
tx-lcn.manager.dtx-time=40000

# 与TxClient通讯最大等待时间（秒）
tx-lcn.message.netty.wait-time=5

# 事务异常通知（任何http协议地址。未指定协议时，为TxManager提供的接口）
tx-lcn.manager.ex-url=/provider/email-to/ujued@qq.com
```

----------------
`注意（NOTE）`   

(1) TxManager所有配置均有默认配置，请按需覆盖默认配置。  

(2) *特别注意* TxManager进程会监听两个端口号，一个为`TxManager端口`，另一个是`事务消息端口`。TxClient默认连接`事务消息端口`是`8070`，
所以，为保证TX-LCN基于默认配置运行良好，请设置`TxManager端口`号为`8069` 或者指定`事务消息端口`为`8070`  

(3) `分布式事务执行总时间 a` 与 `TxClient通讯最大等待时间 b`、`TxManager通讯最大等待时间 c`、`微服务间通讯时间 d`、`微服务调用链长度 e` 几个时间存在着依赖关系。
`a >= 2c + (b + c + d) * (e - 1)`, 特别地，b、c、d 一致时，`a >= (3e-1)b`。你也可以在此理论上适当在减小a的值，发生异常时能更快得到自动补偿，即 `a >= (3e-1)b - Δ`（[原因](../fqa.html)）。
最后，调用链小于等于3时，将基于默认配置运行良好

(4) 若用`tx-lcn.manager.ex-url=/provider/email-to/xxx@xx.xxx` 这个配置，配置管理员邮箱信息(如QQ邮箱)：
```properties
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=xxxxx@qq.com
spring.mail.password=nnvmtplwypuybiof
```
 
----------------