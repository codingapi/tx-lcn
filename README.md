# LCN分布式事务框架v4.0

  "LCN并不生产事务，LCN只是本地事务的搬运工"

## 官方网址

[www.txlcn.org](http://www.txlcn.org)


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

[原理介绍](https://github.com/codingapi/tx-lcn/wiki)  [视频讲解](http://www.txlcn.org/v4/index.html)

## 目录说明

transaction-dubbo LCN dubbo rpc框架扩展支持

transaction-springcloud LCN springcloud rpc框架扩展支持

transaction-motan LCN motan rpc框架扩展支持

tx-client 是LCN核心tx模块端控制框架

tx-manager 是LCN 分布式事务协调器

tx-plugins-db 是LCN 对关系型数据库的插件支持

tx-plugins-nodb 是LCN 对于无数据库模块的插件支持

tx-plugins-redis 是LCN 对于redis模块的插件支持（功能暂未实现）


## 使用说明

分布式事务发起方：

```

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
```

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
  
  若存在业务方法：a->b b->c b->d，那么开启分布式事务注解的话，只需要在a方法上添加@TxTransaction即可。
  
```
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

## demo演示教程

每个demo下有区分为 jdbc/hibernate/mybatis不同框架的版本demo

[springcloud版本](https://github.com/codingapi/springcloud-lcn-demo)

[dubbo版本](https://github.com/codingapi/dubbo-lcn-demo)

[motan版本](https://github.com/zfvipgit/motan-lcn-demo.git)

技术交流群：554855843
