# LCN分布式事务框架v4.0 

  "LCN并不生产事务，LCN只是本地事务的协调者"

 > 5.0.0.beta 版本发布 见5.0.0.beta 分支   


## 框架介绍   

  LCN分布式事务框架的核心功能是对本地事务的协调控制，框架本身并不创建事务，只是对本地事务做协调控制。因此该框架与其他第三方的框架兼容性强，支持所有的关系型数据库事务，支持多数据源，支持与第三方数据库框架一块使用（例如 sharding-jdbc），在使用框架的时候只需要添加分布式事务的注解即可，对业务的侵入性低。LCN框架主要是为微服务框架提供分布式事务的支持，在微服务框架上做了进一步的事务机制优化，在一些负载场景上LCN事务机制要比本地事务机制的性能更好，4.0以后框架开方了插件机制可以让更多的第三方框架支持进来。


## 官方网址

[https://www.txlcn.org](https://www.txlcn.org)


## 框架特点

1. 支持各种基于spring的db框架
2. 兼容SpringCloud、Dubbo、motan
3. 使用简单，低依赖，代码完全开源
4. 基于切面的强一致性事务框架
5. 高可用，模块可以依赖RPC模块做集群化，TxManager也可以做集群化
6. 支持本地事务和分布式事务共存
7. 支持事务补偿机制，增加事务补偿决策提醒
8. 添加插件拓展机制


## 原理介绍

[原理介绍](https://github.com/codingapi/tx-lcn/wiki)  [视频讲解](https://www.txlcn.org/v4/index.html)

## 目录说明

transaction-dubbo LCN dubbo rpc框架扩展支持

transaction-springcloud LCN springcloud rpc框架扩展支持

transaction-motan LCN motan rpc框架扩展支持

tx-client 是LCN核心tx模块端控制框架

tx-manager 是LCN 分布式事务协调器

tx-plugins-db 是LCN 对关系型数据库的插件支持


## 使用说明

分布式事务发起方：

```

    @Override
    @TxTransaction(isStart=true)
    @Transactional
    public boolean hello() {
        //本地调用
        testDao.save();
        //远程调用方
        boolean res =  test2Service.test();
        //模拟异常
        int v = 100/0;
        return true;
    }
    
    
```

分布式事务被调用方(test2Service的业务实现类)
```

    @Override
    @Transactional
    @TxTransaction
    public boolean test() {
        //本地调用
        testDao.save();
        return true;
    }

```

如上代码执行完成以后两个模块都将回滚事务。

说明：在使用LCN分布式事务时，只需要将事务的开始方法添加`@TxTransaction(isStart=true)`注解即可,在参与方添加`@TxTransaction`或者实现`ITxTransaction`接口即可。详细见demo教程

## 关于@TxTransaction 使用说明

  @TxTransaction注解是分布式事务的标示。
  
  若存在业务方法：a->b b->c b->d，那么开启分布式事务注解的话，需要在各个模块方法上添加@TxTransaction即可。
  
```
    @TxTransaction(isStart=true)
    @Transactional
    public void a(){
        b();
    }
    
    @TxTransaction
    public void b(){
        c();
        d();
    }
    
    @TxTransaction
    public void c(){}
    
    @TxTransaction
    public void d(){}
```

## maven 中心库地址


```
<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>tx-client</artifactId>
    <version>${lcn.last.version}</version>
</dependency>


<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>tx-plugins-db</artifactId>
    <version>${lcn.last.version}</version>
</dependency>


<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>transaction-dubbo</artifactId>
    <version>${lcn.last.version}</version>
</dependency>      

<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>transaction-motan</artifactId>
    <version>${lcn.last.version}</version>
</dependency>  


<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>transaction-springcloud</artifactId>
    <version>${lcn.last.version}</version>
</dependency>    
        
```

依赖gradle等形式，见中心库   

[http://mvnrepository.com/search?q=codingapi](http://mvnrepository.com/search?q=codingapi)


## demo演示教程

每个demo下有区分为 jdbc/hibernate/mybatis不同框架的版本demo

[springcloud版本](https://github.com/codingapi/springcloud-lcn-demo)

[dubbo版本](https://github.com/codingapi/dubbo-lcn-demo)

[motan版本](https://gitee.com/zfvipCase/motan-lcn-demo)

[txc模式](https://github.com/caisirius/test-lcn-dubbo)


技术交流群：554855843(已满) 970071379(未满)
