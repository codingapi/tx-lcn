/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.jdbcproxy.p6spy;

import com.codingapi.txlcn.jdbcproxy.p6spy.spring.CompoundJdbcEventListener;
import com.codingapi.txlcn.jdbcproxy.p6spy.event.SimpleJdbcEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author lorne
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
        listeners.forEach((k, v) -> compoundJdbcEventListener.addListener(v));
    }
}
