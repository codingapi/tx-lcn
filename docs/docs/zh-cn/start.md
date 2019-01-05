# 快速开始

## 一、微服务额外依赖`TXLCN`资源管理库
```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>tx-client-springcloud</artifactId>
    <version>5.0.0.beta</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>tx-client-dubbo</artifactId>
    <version>5.0.0.beta</version>
</dependency>
```
> `NOTE` 依微服务架构依赖其一

## 二、三个协作的微服务示例代码
```java
// Micro Service Client. As DTX starter
@LcnTransaction
@Transactional
public String execute(String value) throw BusinessException {
    // step1. call remote service D
    String result1 = microServiceD.rpc(value);
    // step2. call remote service E
    String result2 = microServiceE.rpc(value);
    // step3. local store operate
    valueDao.save(value);
    return return result2 + " > " + result2 + " > " + "ok-client";
}

// Micro Service D
@TxcTransaction
public String rpc(String value) throw BusinessException {
    valueDao.save(value);
    return "ok-D";
}

// Micro Service E
@TccTransaction
public String rpc(String value) throw BusinessException {
    long id = valueDao.save(value);
    globalCache.bind(DTXLocal.cur().groupId(), id);
    return "ok-E";
}

public void confirmRpc(String value) {
    LOG.info("confirm rpc");
    // other confrim logic
}

public void cancelRpc(Stirng value) {
    LOG.info("cancel rpc");
    // rollback by self
    valueDao.deleteById(globalCache.value(DTXLocal.cur().groupId()));
}
```
>`NOTES`  
@LcnTransaction 
标注事务单元用Lcn事务模式参与分布式事务[[原理]](principle/lcn.html)  
@TxcTransaction 
标注事务单元用Txc事务模式参与分布式事务[[原理]](principle/txc.html)  
@TccTransaction 
标注事务单元用Tcc事务模式参与分布式事务[[原理]](principle/tcc.html)  

## 三、启动TxManager

#### 配置项
```properties
server.port=8069
```
> `NOTE` 基于默认配置正常运行请确保TxManager服务端口为8069

----------------------
至此你已经开发好了一个简单的支持分布式事务的分布式微服务系统