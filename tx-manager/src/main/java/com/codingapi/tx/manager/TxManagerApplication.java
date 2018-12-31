package com.codingapi.tx.manager;

import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.manager.support.TxManagerManagerRefreshing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@SpringBootApplication
public class TxManagerApplication {


    public static void main(String[] args) {
        SpringApplication.run(TxManagerApplication.class, args);
    }

    @Bean
    public ProtostuffSerializer protostuffSerializer() {
        return new ProtostuffSerializer();
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Autowired
    private TxManagerManagerRefreshing txManagerManagerRefreshing;

    @PostConstruct
    public void init(){
        txManagerManagerRefreshing.refresh();
    }
}
