# client配置说明

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
```

> `NOTE` TxClient所有配置均有默认配置，请按需覆盖默认配置。