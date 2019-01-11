package com.codingapi.tx.manager.support.message;

import com.codingapi.tx.client.spi.message.RpcAnswer;
import com.codingapi.tx.client.spi.message.RpcClient;
import com.codingapi.tx.client.spi.message.dto.RpcCmd;
import com.codingapi.tx.client.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 * @date 2018/12/10
 * @description
 */
@Service
@Slf4j
public class ServerRpcAnswer implements RpcAnswer {

    private final HashGroupRpcCmdHandler hashGroupRpcCmdHandler;

    private final RpcClient rpcClient;

    @Autowired
    public ServerRpcAnswer(HashGroupRpcCmdHandler hashGroupRpcCmdHandler, RpcClient rpcClient) {
        this.hashGroupRpcCmdHandler = hashGroupRpcCmdHandler;
        this.rpcClient = rpcClient;
    }


    @Override
    public void callback(RpcCmd rpcCmd) {

        try {
            hashGroupRpcCmdHandler.handleMessage(rpcCmd);
        } catch (Throwable e) {
            if (rpcCmd.getKey() != null) {
                log.info("send response.");
                String action = rpcCmd.getMsg().getAction();
                // 事务协调器业务未处理的异常响应服务器失败
                rpcCmd.setMsg(MessageCreator.serverException(action));
                try {
                    rpcClient.send(rpcCmd);
                    log.info("send response ok.");
                } catch (RpcException ignored) {
                    log.error("requester:{} dead.", rpcCmd.getRemoteKey());
                }
            }
        }
    }
}
