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
package com.codingapi.txlcn.manager.initializer;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.manager.db.redis.RedisManagerStorage;
import com.codingapi.txlcn.manager.support.TxManagerAutoCluster;
import com.codingapi.txlcn.manager.support.message.TxLcnManagerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: TxManger检查
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Component
@Slf4j
public class TxManagerInitializer implements TxLcnInitializer {


    @Autowired
    private TxManagerAutoCluster managerAutoCluster;

    @Autowired
    private RedisManagerStorage redisManagerStorage;

    @Autowired
    private TxLcnManagerServer txLcnManagerServer;

    @Override
    public void init() throws Exception {
        
        txLcnManagerServer.init();
        
        redisManagerStorage.init();
        
        // 新增节点 读取redis个节点信息后 通知客户端连接
        managerAutoCluster.refresh();
    }


}
