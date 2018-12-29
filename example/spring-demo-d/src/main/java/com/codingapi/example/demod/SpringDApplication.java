package com.codingapi.example.demod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringDApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDApplication.class, args);
    }
}
