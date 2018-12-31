package com.codingapi.tx.client.spi.rpc.callback;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.client.support.rpc.MessageCreator;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.spi.rpc.ClientInitCallBack;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import com.codingapi.tx.spi.rpc.params.InitClientParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Component
@Slf4j
public class TxClientClientInitCallBack implements ClientInitCallBack {

    @Autowired
    private RpcClient rpcClient;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private TxClientConfig txClientConfig;

    private ExecutorService singleThreadExecutor =  Executors.newSingleThreadExecutor();

    @Override
    public void connected(String remoteKey) {

        singleThreadExecutor.submit(() -> {
            try {
                log.info("send--->{}",remoteKey);
                MessageDto msg =  rpcClient.request(remoteKey, MessageCreator.initClient(appName));
                if(msg.getBytes()!=null){
                    //每一次建立连接时将会获取最新的时间
                    InitClientParams resParams =  msg.loadData(InitClientParams.class);
                    long dtxTime =  resParams.getDtxTime();
                    txClientConfig.setDtxTime(dtxTime);
                    log.info("set dtx time finish. time:{}",dtxTime);
                }
            } catch (RpcException | SerializerException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
