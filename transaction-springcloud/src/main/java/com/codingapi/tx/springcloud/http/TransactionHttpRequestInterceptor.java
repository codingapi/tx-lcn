package com.codingapi.tx.springcloud.http;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by lorne on 2017/7/3.
 */
public class TransactionHttpRequestInterceptor implements ClientHttpRequestInterceptor {


    private Logger logger = LoggerFactory.getLogger(TransactionHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        String groupId = txTransactionLocal == null ? null : txTransactionLocal.getGroupId();
        int maxTimeOut = txTransactionLocal == null ? 0 : txTransactionLocal.getMaxTimeOut();

        logger.info("LCN-SpringCloud TxGroup info -> groupId:"+groupId+",maxTimeOut:"+maxTimeOut);

        if(txTransactionLocal!=null) {
            request.getHeaders().add("tx-group", groupId);
            request.getHeaders().add("tx-maxTimeOut", String.valueOf(maxTimeOut));
        }
        return execution.execute(request,body);
    }
}
