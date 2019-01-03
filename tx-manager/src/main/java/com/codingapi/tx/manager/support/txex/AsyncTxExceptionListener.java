package com.codingapi.tx.manager.support.txex;

import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.db.domain.TxException;
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
