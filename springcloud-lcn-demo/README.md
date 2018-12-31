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

>2.application.properties文件配置eureka地址
```properties
eureka.client.service-url.defaultZone=http://127.0.0.1:8761/eureka/
```

>3.发起方和参与方的业务方法添加@Transactional注解和@TxTransaction注解

>4.发起方中的涉及分布式事务的方法要添加@TxTransaction(isStart = true)注解

>5.tx-manager启动前要配置redis

>6.springboot启动类添加注解配置@EnableFeignClients(basePackages = {"com.codingapi.tx"})


注意事项：避免事务发起方和调用方操作一张表的数据，会导致锁表