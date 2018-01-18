package com.codingapi.tx.motan.filter;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.core.extension.Activation;
import com.weibo.api.motan.core.extension.SpiMeta;
import com.weibo.api.motan.filter.Filter;
import com.weibo.api.motan.rpc.Caller;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import com.weibo.api.motan.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p>motan拦截器</p>
 *
 * @author 张峰 zfvip_it@163.com
 *  2017/11/17 15:38
 */
@SpiMeta(name = "transaction")
@Activation(key = {MotanConstants.NODE_TYPE_SERVICE, MotanConstants.NODE_TYPE_REFERER})
public class TransactionFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(TransactionFilter.class);

    /**
     * 实现新浪的filter接口 rpc传参数
     * @param caller caller
     * @param request 请求
     * @return Response
     */
    public Response filter(Caller<?> caller, Request request) {
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if (txTransactionLocal != null) {

            request.setAttachment("tx-group", txTransactionLocal.getGroupId());

            logger.info("LCN-dubbo TxGroup info -> groupId:"+ txTransactionLocal.getGroupId()+",maxTimeOut:"+txTransactionLocal.getMaxTimeOut());

        } else {
            Map<String, String> map = request.getAttachments();
            if (map != null && !map.isEmpty()) {
                if (map.containsKey("tx-group")) {
                    RpcContext.getContext().putAttribute("tx-group", request.getAttachments().get("tx-group"));
                }
            }
        }


        return caller.call(request);
    }
}
