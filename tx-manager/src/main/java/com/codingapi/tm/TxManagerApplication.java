package com.codingapi.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TxManagerApplication {



    public static void main(String[] args) {
        SpringApplication.run(TxManagerApplication.class, args);
    }

}
