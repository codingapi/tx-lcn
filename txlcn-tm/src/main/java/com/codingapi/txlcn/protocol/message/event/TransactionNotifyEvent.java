package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import com.codingapi.txlcn.tm.loadbalancer.LoadBalancerInterceptor;
import com.codingapi.txlcn.tm.repository.TransactionGroup;
import com.codingapi.txlcn.tm.repository.TransactionGroupRepository;
import com.codingapi.txlcn.tm.repository.TransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
public class TransactionNotifyEvent extends TransactionMessage {

    private String result;

    /**
     * 业务执行结果
     */
    private boolean success;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        LoadBalancerInterceptor loadBalancer = (LoadBalancerInterceptor) springContext.getBean("interceptor");
        loadBalancer.handle(this, protocoler, connection, () -> {
            log.info("request msg =>{}", groupId);
            TransactionGroupRepository transactionGroupRepository =
                    springContext.getBean(TransactionGroupRepository.class);
            TransactionGroup transactionGroup = transactionGroupRepository.notify(groupId, success);
            if (transactionGroup != null) {
                //记录tc请求通知 日志
                List<TransactionInfo> transactionInfoList = transactionGroup.listTransaction();
                TransactionCommitEvent transactionCommitEvent =
                        new TransactionCommitEvent(groupId, transactionGroup.hasCommit());
                transactionInfoList.forEach(transactionInfo ->
                        protocoler.sendMsg(transactionInfo.getUniqueKey(), transactionCommitEvent));

                this.result = "ok";
            } else {
                this.result = "error";
            }
            protocoler.sendMsg(connection.getUniqueKey(), this);
            log.info("request send notify msg =>{}", connection.getUniqueKey());
        });
    }
}
