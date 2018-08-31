package com.codingapi.lcn.tx.demo.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DemoStartApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoStartApplication.class,args);
    }

}
