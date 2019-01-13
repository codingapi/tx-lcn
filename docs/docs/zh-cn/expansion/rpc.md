# RPC框架扩展

为了方便说明以springcloud框架为例说明如何扩展第三方RPC框架。

1. 增加tx-spi-sleuth的扩展实现。

关于sleuth 参考 https://cloud.spring.io/spring-cloud-sleuth/2.0.x/single/spring-cloud-sleuth.html

tx-spi-sleuth的扩展主要控制:重写负载机制、sleuth参数传递

关于srpingcloud的负载机制扩展:

srpingcloud的负载机制是基于ribbon，关于ribbon原理请参考
关于ZoneAvoidanceRule的参考 https://cloud.spring.io/spring-cloud-netflix/2.0.x/single/spring-cloud-netflix.html

```
@Slf4j
@Scope("prototype")
public class TXLCNZoneAvoidanceRule extends ZoneAvoidanceRule {

    //针对sleuth 负载控制的ExtraField参数设置
    private final SleuthParamListener sleuthParamListener;

    private final Registration registration;

    public TXLCNZoneAvoidanceRule(SleuthParamListener sleuthParamListener,
                                  Registration registration) {
        this.sleuthParamListener = sleuthParamListener;
        this.registration = registration;
    }

    @Override
    public Server choose(Object key) {
        return getServer(key);
    }

    private Server getServer(Object key) {
        String localKey = String.format("%s:%s:%s", registration.getServiceId(), registration.getHost(), registration.getPort());
        List<String> appList = sleuthParamListener.beforeBalance(localKey);
        Server balanceServer = null;
        List<Server> servers = getLoadBalancer().getAllServers();
        log.debug("load balanced rule servers: {}", servers);
        for (Server server : servers) {
            for (String appKey : appList) {
                String serverKey = String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort());
                if (serverKey.equals(appKey)) {
                    balanceServer = server;
                }
            }
        }
        if (balanceServer == null) {
            Server server = super.choose(key);
            sleuthParamListener.alfterNewBalance(String.format("%s:%s", server.getMetaInfo().getAppName(), server.getHostPort()));
            return server;
        } else {
            return balanceServer;
        }
    }

}
```

关于srpingcloud的sleuth参数传递扩展:

由于ribbon与sleuth都是基于ClientHttpRequestInterceptor来控制的，但ribbon的拦截器的order小于sleuth，特此处理一下顺序。

```
@Component
public class RibbonFirstRestTemplateCustomizer implements RestTemplateCustomizer {


    private LoadBalancerInterceptor loadBalancerInterceptor;

    @Autowired
    public RibbonFirstRestTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor) {
        this.loadBalancerInterceptor = loadBalancerInterceptor;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
        list.add(0,loadBalancerInterceptor);
        restTemplate.setInterceptors(list);
    }

}
```


2. 提供client端的pom。例如tx-client-springcloud：


```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>tx-lcn</artifactId>
        <groupId>com.codingapi.txlcn</groupId>
        <version>5.0.0.RC1</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <packaging>pom</packaging>
    <artifactId>tx-client-springcloud</artifactId>


    <dependencies>

        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>tx-client</artifactId>
        </dependency>

        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>tx-spi-sleuth-springcloud</artifactId>
        </dependency>

    </dependencies>

</project>

```