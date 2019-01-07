# client配置说明
## 一、application.properties
```properties
# 事务控制切面控制次序
tx-lcn.client.control-order=0

# 资源切面次序
tx-lcn.client.resource-order=0

# manager服务地址(rpc端口),可填写多个负载
tx-lcn.client.manager-address=127.0.0.1:8070

# 切面日志信息(h2数据库地址)
tx-lcn.aspect.log.file-path=${user.dir}/.txlcn/${spring.application.name}

# 开启日志数据库记录存储
tx-lcn.logger.enabled=true

# 日志数据库存储jdbc配置
tx-lcn.logger.driver-class-name=com.mysql.jdbc.Driver
tx-lcn.logger.jdbc-url=jdbc:mysql://127.0.0.1:3306/tx-logger?\
  characterEncoding=UTF-8&serverTimezone=UTC
tx-lcn.logger.username=root
tx-lcn.logger.password=123456

# 与TxManager通讯最大等待时间（秒）
tx-lcn.message.netty.wait-time=5

```

## 二、特别配置
微服务`集群`且用到 `LCN` 事务模式时，为保证性能请开启 `TXLCN` 重写的负载策略。  

* Dubbo 开启
```$xslt
@Reference(version = "${demo.service.version}",
        application = "${dubbo.application.e}",
        retries = -1,
        registry = "${dubbo.registry.address}",
        loadbalance = "txlcn_random")  // here
private EDemoService eDemoService;
```
* SpringCloud 开启 (application.properties)
```properties
tx-lcn.springcloud.loadbalance.enabled=true
```

配置详情[参见](distributed.html)

----------------

`NOTE` TxClient所有配置均有默认配置，请按需覆盖默认配置。

----------------