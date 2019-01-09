# 示例

## 一、Mariadb (MySQL) 数据库
[txlcn-demo.sql](https://github.com/codingapi/tx-lcn/blob/5.0.0-dev/example/txlcn-demo.sql)  
[tx-manager.sql](https://github.com/codingapi/tx-lcn/blob/5.0.0-dev/tx-manager/src/main/resources/tx-manager.sql)

## 二、TxManager
此位置的[TxManger服务](https://github.com/codingapi/tx-lcn/tree/5.0.0-dev/tx-manager)基于如下配置打包部署

```properties
spring.application.name=tx-manager
server.port=8069

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://ip:port/tx-manager?characterEncoding=UTF-8
spring.datasource.username=user
spring.datasource.password=passwd

mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.use-generated-keys=true

```
![tx-manager](img/tx_manager.png)

## 三、注册中心
* 启动ZooKeeper (Dubbo)
* 启动Consul (SpringCloud)

## 四、微服务代码（TxClient）
[Dubbo-Demo](dubbo.html)

[SpringCloud-Demo](springcloud.html)

## 五、检验微服务间的分布式事务
（1）正常提交事务

访问 发起方提供的Rest接口 `/txlcn?value=the-value`。发现事务全部提交  
![result](img/result.png)

（2）回滚事务

修改微服务 发起方Client 业务，在返回结果前抛出异常，再请求Rest接口。发现发起方由于本地事务回滚，而参与方D、E，由于TX-LCN的协调，数据也回滚了。  
![error_result](img/error-result.png)

## 六、结束