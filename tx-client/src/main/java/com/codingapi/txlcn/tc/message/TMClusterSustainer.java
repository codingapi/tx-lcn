package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.support.listener.RpcEnvStatusListener;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Date: 1/27/19
 *
 * @author ujued
 */
@Component
@Slf4j
public class TMClusterSustainer implements RpcEnvStatusListener {

    private AtomicInteger initCount = new AtomicInteger(0);

    private final TxClientConfig txClientConfig;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    public TMClusterSustainer(TxClientConfig txClientConfig, ReliableMessenger reliableMessenger) {
        this.txClientConfig = txClientConfig;
        this.reliableMessenger = reliableMessenger;
    }

    @Override
    public void onConnected(String remoteKey) {

    }

    @Override
    public void onInitialized(String remoteKey) {
        prepareToResearchTMCluster();
    }

    @Override
    public void onConnectFail(String remoteKey) {
        try {
            reliableMessenger.reportInvalidTM(Sets.newHashSet(remoteKey));
        } catch (RpcException e) {
            log.error("{} on reportInvalidTM.", e.getMessage());
        }
        prepareToResearchTMCluster();
    }


    private void prepareToResearchTMCluster() {
        int count = initCount.incrementAndGet();
        int size = txClientConfig.getManagerAddress().size();
        if (count == size) {
            TMSearcher.search();
        } else if (count > size) {
            TMSearcher.searchedOne();
        }
    }
}
