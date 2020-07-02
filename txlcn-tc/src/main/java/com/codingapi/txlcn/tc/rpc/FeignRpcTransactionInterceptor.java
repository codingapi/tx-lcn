package com.codingapi.txlcn.tc.rpc;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
public class FeignRpcTransactionInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        RpcTransactionContext.getInstance().build(requestTemplate::header);
    }
}
