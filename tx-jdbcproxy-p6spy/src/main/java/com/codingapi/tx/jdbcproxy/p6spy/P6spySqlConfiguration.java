package com.codingapi.tx.jdbcproxy.p6spy;

import com.codingapi.tx.jdbcproxy.p6spy.spring.CompoundJdbcEventListener;
import com.codingapi.tx.jdbcproxy.p6spy.event.SimpleJdbcEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author lorne
 * @date 2018/12/12
 * @description
 */
@Configuration
@ComponentScan
public class P6spySqlConfiguration {

    @Autowired
    private ApplicationContext spring;

    @Autowired
    private CompoundJdbcEventListener compoundJdbcEventListener;


    @PostConstruct
    public void init(){
        Map<String, SimpleJdbcEventListener> listeners = spring.getBeansOfType(SimpleJdbcEventListener.class);
        listeners.forEach((k, v) -> compoundJdbcEventListener.addListender(v));
    }
}
