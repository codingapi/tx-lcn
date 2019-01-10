# 快速开始

## 一、微服务额外依赖TX-LCN Client 代码库

springcloud pom
```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>tx-client-springcloud</artifactId>
    <version>5.0.0.beta</version>
</dependency>
```
dubbo pom
```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>tx-client-dubbo</artifactId>
    <version>5.0.0.beta</version>
</dependency>
```
> `NOTE` 依微服务架构依赖其一

## 二、微服务示例代码

ServiceA 事务发起方   
ServiceB 事务参与方(被调方)  
`ServiceA->ServiceB`

ServiceA调用ServiceB.rpc(String value)方法   
`ServiceA=>ServiceB.rpc(String value) `


### (1) 微服务A
```java
// Micro Service A. As DTX starter
@Service
public class ServiceA {
    
    @Autowired
    private ValueDao valueDao;
    @Autowired
    private ServiceB serviceB;
    
    @LcnTransaction
    @Transactional
    public String execute(String value) throws BusinessException {
        // step1. call remote service B
        String result = serviceB.rpc(value);  // (1)
        // step2. local store operate. DTX commit if save success, rollback if not.
        valueDao.save(value);  // (2)
        valueDao.saveBackup(value);  // (3)
        return result + " > " + "ok-A";
    }
}
```
### (2) 微服务B
```java
// Micro Service D
@Service
public class ServiceB {
    @Autowired
    private ValueDao valueDao;
    
    @LcnTransaction
    @Transactional
    public String rpc(String value) throws BusinessException {
        valueDao.save(value);  // (4)
        valueDao.saveBackup(value);  // (5)
        return "ok-B";
    }
}
```

(1) 服务A作为DTX发起方，远程调用服务B  
(2)与(3) 构成A服务本地事务  
(4)与(5) 构成B服务本地事务  

>`NOTES`  
1、@LcnTransaction 
标注事务单元用Lcn事务模式参与分布式事务[[原理]](principle/lcn.html)。还有 
[TXC](principle/txc.html)  [TCC](principle/tcc.html) 模式。  
2、参数配置见 [TxClient配置](setting/client.html)    
3、详细配置见 [dubbo示例](demo/dubbo.html)  [springcloud示例](demo/springcloud.html)

## 三、TxManager配置

配置TxManager参数并启动     
参数配置见 [TxManager配置](setting/manager.html)

----------------------
至此，你已经开发好了一个简单的、支持分布式事务(DTX)的微服务系统