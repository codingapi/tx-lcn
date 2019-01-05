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

## 二、微服务示例代码

### (1) 微服务Client
```java
// Micro Service Client. As DTX starter
@Service
public class ServiceClient {
    private ValueDao valueDao;
    private ServiceD serviceD;
    public ServiceClient(ValueDao valueDao, ServiceD serviceD) {
        this.valueDao = valueDao;
        this.serviceD = serviceD;
    }
    
    @LcnTransaction
    @Transactional
    public String execute(String value) throws BusinessException {
        // step1. call remote service D
        String result = serviceD.rpc(value);
        // step2. local store operate. DTX commit if save success, rollback if not.
        valueDao.save(value);
        valueDao.saveBak(value);
        return result + " > " + "ok-client";
    }
}
```
### (2) 微服务D
```java
// Micro Service D
@Service
public class ServiceD {
    private ValueDao valueDao;
    public ServiceD(ValueDao valueDao) {
        this.valueDao = valueDao;
    }
    
    @LcnTransaction
    @Transactional
    public String rpc(String value) throws BusinessException {
        valueDao.save(value);
        valueDao.saveBak(value);
        return "ok-D";
    }
}
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