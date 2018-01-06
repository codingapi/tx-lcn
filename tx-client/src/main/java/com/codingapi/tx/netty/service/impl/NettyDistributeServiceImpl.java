package com.codingapi.tx.netty.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.model.TxServer;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.codingapi.tx.netty.service.NettyDistributeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class NettyDistributeServiceImpl implements NettyDistributeService {

    private int connectCont = 0;

    private Logger logger = LoggerFactory.getLogger(NettyDistributeServiceImpl.class);

    @Autowired
    private MQTxManagerService txManagerService;

    @Override
    public synchronized void loadTxServer() {
        if (Constants.txServer == null) {
            getTxServer();
            return;
        }
        connectCont++;
        if (connectCont == 3) {
            getTxServer();
        }
    }

    private void getTxServer() {
        //获取负载均衡服务地址
        String json = null;
        while (StringUtils.isEmpty(json)) {
            json = txManagerService.httpGetServer();
            logger.info("get txManager ->" + json);
            if (StringUtils.isEmpty(json)) {
                logger.error("TxManager服务器无法访问.");
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        TxServer txServer = TxServer.parser(json);
        if (txServer != null) {
            logger.debug("txServer -> " + txServer);
            logger.info(txServer.toString());
            Constants.txServer = txServer;
            logger.info(Constants.txServer.toString());
            connectCont = 0;
        }

    }

}
