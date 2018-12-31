package com.codingapi.tx.client.spi.rpc.callback;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.client.support.rpc.MessageCreator;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.rpc.params.InitClientParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.spi.rpc.ClientInitCallBack;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private ProtostuffSerializer protostuffSerializer;

    @Autowired
    private TxClientConfig txClientConfig;

    @Override
    public void connected(String remoteKey) {
        new Thread(() -> {
            try {
                log.info("send--->{}",remoteKey);
                MessageDto msg =  rpcClient.request(remoteKey, MessageCreator.initClient(appName));
                if(msg.getBytes()!=null){
                    //每一次建立连接时将会获取最新的时间
                    InitClientParams resParams =  protostuffSerializer.deSerialize(msg.getBytes(), InitClientParams.class);
                    long dtxTime =  resParams.getDtxTime();
                    txClientConfig.setDtxTime(dtxTime);
                    log.info("set dtx time finish. time:{}",dtxTime);
                }
            } catch (RpcException | SerializerException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
