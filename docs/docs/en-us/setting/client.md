# TxClient配置说明
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
### 1、微服务`集群`且用到 LCN事务模式时，为保证性能请开启TX-LCN重写的负载策略。

* Dubbo 开启
```java
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

### 2、关闭业务RPC重试
* Dubbo 开启
```java
@Reference(version = "${demo.service.version}",
        application = "${dubbo.application.e}",
        retries = -1,
        registry = "${dubbo.registry.address}",
        loadbalance = "txlcn_random")  // here
private EDemoService eDemoService;
```
* SpringCloud 开启 (application.properties)
```properties
# 关闭Ribbon的重试机制
ribbon.MaxAutoRetriesNextServer=0
```


----------------

`NOTE`  
1、TxClient所有配置均有默认配置，请按需覆盖默认配置。  
2、为什么要关闭服务调用的重试。远程业务调用失败有两种可能：
（1），远程业务执行失败 （2）、远程业务执行成功，网络失败。对于第2种，事务场景下重试会发生，某个业务执行两次的问题。
如果业务上控制某个事务接口的幂等，则不用关闭重试。

----------------

### 3、当通过AOP配置本地事务时需要调整优先级
   
可以采用两种方式

1. 通过DTXInterceptor配置本地事务.

```
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionConfiguration {

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager, DTXLogicWeaver dtxLogicWeaver) {
        Properties properties = new Properties();
        properties.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");

        DTXInterceptor dtxInterceptor = new DTXInterceptor(dtxLogicWeaver);
        dtxInterceptor.setTransactionManager(transactionManager);
        dtxInterceptor.setTransactionAttributes(properties);
        return dtxInterceptor;
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("transactionInterceptor");
        beanNameAutoProxyCreator.setBeanNames("*Impl");
        return beanNameAutoProxyCreator;
    }
}

```
2. 设置TXLCNInterceptor拦截器，然后设置优先与本地事务

```java
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionConfiguration {


    @Bean
    public TXLCNInterceptor txlcnInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        TXLCNInterceptor dtxInterceptor = new TXLCNInterceptor(dtxLogicWeaver);
        return dtxInterceptor;
    }

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        Properties properties = new Properties();
        properties.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");

        TransactionInterceptor dtxInterceptor = new TransactionInterceptor();
        dtxInterceptor.setTransactionManager(transactionManager);
        dtxInterceptor.setTransactionAttributes(properties);
        return dtxInterceptor;
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("txlcnInterceptor","transactionInterceptor");
        beanNameAutoProxyCreator.setBeanNames("*Impl");
        return beanNameAutoProxyCreator;
    }
}

```