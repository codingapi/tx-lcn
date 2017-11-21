package com.codingapi.tx.netty.service;

/**
 * create by lorne on 2017/11/17
 */
public interface TxManagerHttpRequestService {

     String httpGet(String url);

     String httpPost(String url, String params);

}
