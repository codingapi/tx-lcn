>1.maven引入
````xml
          <dependency>
              <groupId>com.codingapi</groupId>
              <artifactId>transaction-springcloud</artifactId>
              <version>${lcn.last.version}</version>
              <exclusions>
                  <exclusion>
                      <groupId>org.slf4j</groupId>
                      <artifactId>*</artifactId>
                  </exclusion>
              </exclusions>
          </dependency>          
          <dependency>
              <groupId>com.codingapi</groupId>
              <artifactId>tx-plugins-db</artifactId>
              <version>${lcn.last.version}</version>
              <exclusions>
                  <exclusion>
                      <groupId>org.slf4j</groupId>
                      <artifactId>*</artifactId>
                  </exclusion>
              </exclusions>
          </dependency>
````

>2.application.properties文件配置eureka地址和Txmanager地址，其中eureka地址与Txmanager的eureka地址保持一致
```properties
tm.manager.url=http://127.0.0.1:8899/tx/manager/
eureka.client.service-url.defaultZone=http://127.0.0.1:8761/eureka/
```

>3.实现TxManagerHttpRequestService和TxManagerTxUrlService，参考service下的实现

>4.事务参与方(事务发起方不需要)中的业务service要实现ITxTransaction接口

>5.事务发起方(事务参与方不需要)中的涉及分布式事务的方法要添加@TxTransaction(isStart = true)注解

>6.无论事务发起方还是事务参与方的业务方法都需要实现本地事务添加@Transactional注解

>7.txmanager启动前要配置redis


注意事项：避免事务发起方和调用方操作一张表的数据，会导致锁表