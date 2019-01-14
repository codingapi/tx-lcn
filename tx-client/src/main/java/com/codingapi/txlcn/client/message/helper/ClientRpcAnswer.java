package com.codingapi.txlcn.client.message.helper;

import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.client.support.LCNTransactionBeanHelper;
import com.codingapi.txlcn.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description: TxClient对RPC命令回复
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class ClientRpcAnswer implements RpcAnswer {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    private final RpcClient rpcClient;

    @Autowired
    public ClientRpcAnswer(LCNTransactionBeanHelper transactionBeanHelper, RpcClient rpcClient) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.rpcClient = rpcClient;
    }

    @Override
    public void callback(RpcCmd rpcCmd) {
        log.debug("Receive Message: {}", rpcCmd.getMsg());
        TransactionCmd transactionCmd = MessageParser.parser(rpcCmd);
        String transactionType = transactionCmd.getTransactionType();
        String action = transactionCmd.getMsg().getAction();
        RpcExecuteService executeService =
                transactionBeanHelper.loadRpcExecuteService(transactionType, transactionCmd.getType());
        MessageDto messageDto = null;
        try {
            Object message = executeService.execute(transactionCmd);
            messageDto = MessageCreator.notifyUnitOkResponse(message,action);
        } catch (TxClientException e) {
            log.error("message > execute error.", e);
            messageDto = MessageCreator.notifyUnitFailResponse(e,action);
        } finally {
            if (Objects.nonNull(rpcCmd.getKey())) {
                try {
                    rpcCmd.setMsg(messageDto);
                    rpcClient.send(rpcCmd);
                } catch (RpcException e) {
                    log.error("response request[{}] error. error message: {}", rpcCmd.getKey(), e.getMessage());
                }
            }
        }
    }
}
