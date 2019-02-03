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
package com.codingapi.txlcn.common.runner;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: TxLcn Runner
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
public class TxLcnApplicationRunner implements ApplicationRunner, DisposableBean {
    
    private final ApplicationContext applicationContext;
    
    private List<TxLcnInitializer> initializers;
    
    @Autowired
    public TxLcnApplicationRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, TxLcnInitializer> runnerMap = applicationContext.getBeansOfType(TxLcnInitializer.class);
        initializers = runnerMap.values().stream().sorted(Comparator.comparing(TxLcnInitializer::order))
                .collect(Collectors.toList());
        
        for (TxLcnInitializer txLcnInitializer : initializers) {
            txLcnInitializer.init();
        }
    }
    
    @Override
    public void destroy() throws Exception {
        for (TxLcnInitializer txLcnInitializer : initializers) {
            txLcnInitializer.destroy();
        }
    }
    
}
