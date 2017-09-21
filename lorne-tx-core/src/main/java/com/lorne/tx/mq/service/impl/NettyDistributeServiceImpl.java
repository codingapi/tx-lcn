package com.lorne.tx.mq.service.impl;

import com.lorne.core.framework.utils.config.ConfigUtils;
import com.lorne.core.framework.utils.http.HttpUtils;
import com.lorne.tx.Constants;
import com.lorne.tx.mq.model.TxServer;
import com.lorne.tx.mq.service.NettyDistributeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class NettyDistributeServiceImpl implements NettyDistributeService {

    private int connectCont = 0;

    private Logger logger = LoggerFactory.getLogger(NettyDistributeServiceImpl.class);

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
            String url = ConfigUtils.getString("tx.properties", "url");
            //获取服务器ip
            json = HttpUtils.get(url);
            logger.info("获取manager服务信息->" + json);
            if (StringUtils.isEmpty(json)) {
                logger.info("TxManager服务器无法访问.");
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        TxServer txServer = TxServer.parser(json);
        if (txServer != null) {
            logger.info("txServer is " + (txServer == null ? "null" : "not null"));
            logger.info(txServer.toString());
            Constants.txServer = txServer;
            logger.info(Constants.txServer.toString());
            connectCont = 0;
        }

    }

}
