package com.codingapi.tx.netty.service;


import com.lorne.core.framework.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/17
 */
@Component
public class TxManagerHttpRequestHelper {


    private TxManagerHttpRequestService httpRequestService;

    @Autowired
    private ApplicationContext spring;

    private Logger logger = LoggerFactory.getLogger(TxManagerHttpRequestHelper.class);


    private void reloadHttpRequestService(){
        try {
            httpRequestService = spring.getBean(TxManagerHttpRequestService.class);
        }catch (Exception e){
            logger.debug("load default httpRequestService ");
        }

        if(httpRequestService==null){
            httpRequestService = new TxManagerHttpRequestService() {
                @Override
                public String httpGet(String url) {
                    return HttpUtils.get(url);
                }

                @Override
                public String httpPost(String url, String params) {
                    return HttpUtils.post(url, params);
                }
            };
            logger.info("load default HttpRequestService .");
        }else {
            logger.info("load HttpRequestService .");
        }
    }

    public String httpGet(String url) {
        reloadHttpRequestService();
        return httpRequestService.httpGet(url);
    }

    public String httpPost(String url, String params) {
        reloadHttpRequestService();
        return httpRequestService.httpPost(url,params);
    }


}
