package com.codingapi.tx.client.support.rpc;

import com.codingapi.tx.spi.message.RpcAnswer;
import com.codingapi.tx.spi.message.RpcClient;
import com.codingapi.tx.spi.message.dto.MessageDto;
import com.codingapi.tx.spi.message.dto.RpcCmd;
import com.codingapi.tx.spi.message.exception.RpcException;
import com.codingapi.tx.client.support.LCNTransactionBeanHelper;
import com.codingapi.tx.commons.exception.TxClientException;
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
        RpcExecuteService executeService =
                transactionBeanHelper.loadRpcExecuteService(transactionType, transactionCmd.getType());
        MessageDto messageDto = null;
        try {
            Object message = executeService.execute(transactionCmd);
            messageDto = MessageCreator.notifyUnitOkResponse(message);
        } catch (TxClientException e) {
            log.error("message > execute error.", e);
            messageDto = MessageCreator.notifyUnitFailResponse(e);
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
