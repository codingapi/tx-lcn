# LCN分布式事务框架

  "LCN并不生产事务，LCN只是本地事务的搬用工"


## maven 中心库地址

```

<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>tx-client</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>tx-plugins-nodb</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>tx-plugins-db</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
	<groupId>com.codingapi</groupId>
	<artifactId>transaction-springcloud</artifactId>
	<version>1.0.0</version>
</dependency>


<dependency>
    <groupId>com.codingapi</groupId>
    <artifactId>transaction-dubbo</artifactId>
    <version>1.0.0</version>
</dependency>

```

## 原理介绍

[LCN原理](https://github.com/codingapi/tx-lcn/wiki/LCN原理)


## demo教程

每个demo下有区分为 jdbc/hibernate/mybatis不同框架的版本demo

[springcloud版本](https://github.com/codingapi/springcloud-lcn-demo)

[dubbo版本](https://github.com/codingapi/dubbo-lcn-demo)


更多资料维护中...