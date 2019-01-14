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
package com.codingapi.txlcn.manager.support.txex;

import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.db.domain.TxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;

/**
 * Description:
 * Date: 19-1-3 上午9:44
 *
 * @author ujued
 */
@Component
public class AsyncTxExceptionListener implements TxExceptionListener {

    private final TxManagerConfig txManagerConfig;

    private final RestTemplate restTemplate;

    @Value("${server.port}")
    private Integer managerServicePort = 8083;

    private final ExecutorService executorService;

    @Autowired
    public AsyncTxExceptionListener(TxManagerConfig txManagerConfig,
                                    RestTemplate restTemplate, ExecutorService executorService) {
        this.txManagerConfig = txManagerConfig;
        this.restTemplate = restTemplate;
        this.executorService = executorService;
    }

    @Override
    public void onException(TxException txException) {
        if (txManagerConfig.isExUrlEnabled()) {
            executorService.submit(() -> {
                try {
                    if (!txManagerConfig.getExUrl().startsWith("http")) {
                        txManagerConfig.setExUrl("http://127.0.0.1:" + managerServicePort + txManagerConfig.getExUrl());
                    }
                    String result = restTemplate.postForObject(txManagerConfig.getExUrl(), txException, String.class);
                } catch (Exception ignored) {
                }
            });
        }
    }
}
