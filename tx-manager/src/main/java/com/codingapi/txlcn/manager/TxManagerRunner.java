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
package com.codingapi.txlcn.manager;

import com.codingapi.txlcn.commons.init.TxLcnRunner;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 计划统一入口管理lcn配置  暂未使用
 *
 * @author meetzy
 */
public class TxManagerRunner implements ApplicationRunner, DisposableBean {
    
    private ApplicationContext applicationContext;
    private List<TxLcnRunner> runners;
    
    @Autowired
    public TxManagerRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 可以考虑控制执行顺序 rpc启动在前 写入redis在后
        Map<String, TxLcnRunner> maps = applicationContext.getBeansOfType(TxLcnRunner.class);
        runners = new ArrayList<>(maps.values());
        for (TxLcnRunner runner : runners) {
            runner.init();
        }
    }
    
    @Override
    public void destroy() throws Exception {
        for (TxLcnRunner runner : runners) {
            runner.destroy();
        }
    }
}
