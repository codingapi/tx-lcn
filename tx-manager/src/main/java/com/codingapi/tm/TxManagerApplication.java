package com.codingapi.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class TxManagerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = null;
        try {
            context = SpringApplication.run(TxManagerApplication.class, args);
        } finally {
           close(context);
        }

    }

    private static void close(final ConfigurableApplicationContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (context != null) {
                    context.close();
                }
            }
        });
    }

}
