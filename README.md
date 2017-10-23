# LCN分布式事务框架

## 框架特点

1. 支持各种基于spring的db框架
2. 兼容springcloud、dubbo
3. 使用简单，低依赖，代码完全开源
4. 基于切面的强一致性事务框架
5. 高可用，模块可以依赖dubbo或springcloud的集群方式做集群化，TxManager也可以做集群化
6. 支持本地事务和分布式事务共存
7. 事务补偿机制，服务故障或挂机再启动时可恢复事务


## 注意事项

1. 同一个模块单元下的事务是嵌套的。
2. 不同事务模块下的事务是独立的。

备注：框架在多个业务模块下操作更新同一个库下的同一张表下的同一条时，将会出现锁表的情况，会导致分布式事务异常，数据会丧失一致性。

方案：
  希望开发者在设计模块时避免出现多模块更新操作（insert update delete）同一条数据的情况。
  
3. 禁止重名的bean对象。
  事务的补偿机制是基于java反射的方式重新执行一次需要补偿的业务。因此执行的时候需要获取到业务的service对象，LCN是基于spring的ApplicationContent的getBean方法获取bean的对象的。因此不允许出现重名对象。
  


## 使用示例

引入maven文件，根据框架选择springcloud或者dubbo版本

```
    <dependency>
        <groupId>com.github.1991wangliang</groupId>
        <artifactId>springcloud-transaction</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <dependency>
        <groupId>com.github.1991wangliang</groupId>
        <artifactId>dubbo-transaction</artifactId>
        <version>1.0.0</version>
    </dependency>

```

分布式事务发起方：
```java

    @Override
    @TxTransaction
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
```java

    @Override
    @Transactional
    public boolean test() {
        //本地调用
        testDao.save();
        return true;
    }

```

如上代码执行完成以后两个模块都将回滚事务。

说明：在使用LCN分布式事务时，只需要将事务的开始方法添加`@TxTransaction`注解即可。详细见demo教程

## 关于@TxTransaction 使用说明

  @TxTransaction注解是分布式事务的标示。
  
  若存在业务方法：a-b b-c b-d，那么开启分布式事务注解的话，只需要在a方法上添加@TxTransaction即可。
  
```java
    @TxTransaction
    @Transactional
    public void a(){
        b();
    }

    public void b(){
        c();
        d();
    }

    public void c(){}

    public void d(){}
```

## 目录说明

lorne-tx-core 是LCN分布式事务框架的切面核心类库，支持关系型数据库分布式事务

lorne-tx-core-redis 是LCN分布式事务框架对Redis数据库的分布式事务扩展支持

dubbo-transaction 是LCN dubbo分布式事务框架

springcloud-transaction 是LCN springcloud分布式事务框架

tx-manager 是LCN 分布式事务协调器


## 关于框架的设计原理

见 [TxManager](https://github.com/1991wangliang/tx-lcn/blob/master/tx-manager/README.md)


## demo 说明

demo里包含jdbc\hibernate\mybatis版本的demo

dubbo版本的demo [dubbo-demo](https://github.com/1991wangliang/dubbo-lcn-demo)

springcloud版本的demo [springcloud-demo](https://github.com/1991wangliang/springcloud-lcn-demo)


技术交流群：554855843
