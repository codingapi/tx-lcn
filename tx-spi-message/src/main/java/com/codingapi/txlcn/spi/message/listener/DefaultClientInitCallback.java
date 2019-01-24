package com.codingapi.txlcn.spi.message.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Date: 19-1-24 下午6:06
 *
 * @author ujued
 */
@Slf4j
public class DefaultClientInitCallback implements ClientInitCallBack {

    @Override
    public void connected(String remoteKey, int clusterSize) {
        log.info("client {} connected", remoteKey);
    }

    @Override
    public void disconnected(String remoteKey) {
        log.info("client {} disconnected", remoteKey);
    }
}
