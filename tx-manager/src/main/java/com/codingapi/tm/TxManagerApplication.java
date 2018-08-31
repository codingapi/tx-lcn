package com.codingapi.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class TxManagerApplication {



    public static void main(String[] args) {
        SpringApplication.run(TxManagerApplication.class, args);
    }

}
