# 快速开始

## 一、微服务额外依赖`TXLCN` Client 代码库
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

## 二、微服务示例代码

### (1) 微服务A
```java
// Micro Service A. As DTX starter
public class ServiceClient {
    private ValueDao valueDao;
    private ServiceB serviceB;
    public ServiceClient(ValueDao valueDao, ServiceB serviceB) {
        this.valueDao = valueDao;
        this.serviceB = serviceB;
    }
    
    @LcnTransaction
    @Transactional
    public String execute(String value) throws BusinessException {
        // step1. call remote service B
        String result = serviceB.rpc(value);  // (1)
        // step2. local store operate. DTX commit if save success, rollback if not.
        valueDao.save(value);  // (2)
        valueDao.saveBackup(value);  // (3)
        return result + " > " + "ok-client";
    }
}
```
### (2) 微服务B
```java
// Micro Service D
public class ServiceD {
    private ValueDao valueDao;
    public ServiceD(ValueDao valueDao) {
        this.valueDao = valueDao;
    }
    
    @LcnTransaction
    @Transactional
    public String rpc(String value) throws BusinessException {
        valueDao.save(value);  // (4)
        valueDao.saveBackup(value);  // (5)
        return "ok-D";
    }
}
```
```
(1) 服务A作为DTX发起方，远程调用服务B  
(2)与(3) 构成A服务本地事务  
(4)与(5) 构成B服务本地事务  
```

>`NOTES`  
1、@LcnTransaction 
标注事务单元用Lcn事务模式参与分布式事务[[原理]](principle/lcn.html)。还有 
[TXC](principle/txc.html)  [TCC](principle/tcc.html) 模式。  
2、LCN模式需要加本地事务注解，LCN只协调本地事务

## 三、启动TxManager

#### 配置项
```properties
server.port=8069
```
> `NOTE` 基于默认配置正常运行请确保TxManager服务端口为8069 [原因](setting/manager.html)

----------------------
至此，你已经开发好了一个简单的、支持分布式事务(DTX)的微服务系统