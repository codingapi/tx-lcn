### 构建镜像到本地
```
# 当前路径 tx-lcn/txlcn-tm
mvn clean package -DskipTests docker:build
```

#### 运行说明
- 可以不用构建,在有docker环境的机器上直接运行下面命令

1 、 命令行传参数运行

```
docker run -p 7970:7970 -p 8070:8070 --restart always --name tm -e spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx_manager -e spring.datasource.username=root -e spring.datasource.password=root -e spring.redis.host=127.0.0.1 -e spring.redis.port=6379 -e spring.redis.password= -e tx-lcn.manager.admin-key=123456 -d codingapi/txlcn-tm
```
- 说明
- -p 端口映射 宿主机器端口:容器内端口
- --name : 容器别名
-  --restart always : 容器伴随docker服务启动(如果docker是开机启动,那么这个容器就是开机启动的)
- -d : 放入后台运行
- -e 相当于 java -jar tm.jar 后面的参数,
- spring.datasource.url 这个配置里面如果有特殊符号 命令行不支持,建议使用第二种方式

2 、 增加外部配置文件运行. 需要在宿主机器上有文件 /opt/data/lcntm/application-dev.properties
```
spring.application.name=tx-manager
server.port=7970
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx-manager?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root
#mybatis.configuration.map-underscore-to-camel-case=true
#mybatis.configuration.use-generated-keys=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto=none
#tx-lcn.logger.enabled=true
# TxManager Host Ip
tx-lcn.manager.host=127.0.0.1
# TxClient连接请求端口
tx-lcn.manager.port=8070
tx-lcn.manager.admin-key=123456

# 心跳检测时间(ms)
#tx-lcn.manager.heart-time=15000
# 分布式事务执行总时间
#tx-lcn.manager.dtx-time=30000
#参数延迟删除时间单位ms
#tx-lcn.message.netty.attr-delay-time=10000
#tx-lcn.manager.concurrent-level=128
# 开启日志
#tx-lcn.logger.enabled=true
#logging.level.com.codingapi=debug
#redisIp
spring.redis.host=127.0.0.1
#redis\u7AEF\u53E3
spring.redis.port=6379
#redis\u5BC6\u7801
#spring.redis.password=
```
执行命令
```
docker run -p 7970:7970 -p 8070:8070 --restart always --name tm -v /opt/data/lcntm:/opt/data/lcntm -e spring.profiles.active=dev -e spring.config.additional-location=/opt/data/lcntm/application-dev.properties -d codingapi/txlcn.tm
```
- 说明
- -v : 文件挂载
