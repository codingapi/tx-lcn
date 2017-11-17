package com.codingapi.tx.netty.service;


import com.lorne.core.framework.utils.http.HttpUtils;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2017/11/17
 */
@Component
public class TxManagerHttpRequestHelper {


    private HttpRequestService httpRequestService;

    public void setHttpRequestService(HttpRequestService httpRequestService) {
        this.httpRequestService = httpRequestService;
    }

    private void reloadHttpRequestService(){
        if(httpRequestService==null){
            httpRequestService = new HttpRequestService() {
                @Override
                public String httpGet(String url) {
                    return HttpUtils.get(url);
                }

                @Override
                public String httpPost(String url, String params) {
                    return HttpUtils.post(url, params);
                }
            };
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
