package com.codingapi.txlcn.manager.core.message;

import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.logger.TxLogger;
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
    private TxLogger txLogger;

    @Autowired
    public ServerRpcAnswer(HashGroupRpcCmdHandler hashGroupRpcCmdHandler, RpcClient rpcClient) {
        this.hashGroupRpcCmdHandler = hashGroupRpcCmdHandler;
        this.rpcClient = rpcClient;
    }


    @Override
    public void callback(RpcCmd rpcCmd) {

        txLogger.trace(rpcCmd.getMsg().getGroupId(),"","rpccmd",rpcCmd.getMsg().getAction());

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
