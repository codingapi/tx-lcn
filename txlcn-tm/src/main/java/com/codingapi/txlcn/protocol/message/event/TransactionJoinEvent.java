package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import com.codingapi.txlcn.tm.loadbalancer.LoadBalancerInterceptor;
import com.codingapi.txlcn.tm.repository.TransactionGroupRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class TransactionJoinEvent extends TransactionMessage {

    private String result;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        LoadBalancerInterceptor loadBalancer = (LoadBalancerInterceptor) springContext.getBean("interceptor");
        loadBalancer.handle(this, protocoler, connection, () -> {
            TransactionGroupRepository transactionGroupRepository =
                    springContext.getBean(TransactionGroupRepository.class);
            log.info("request msg =>{}", groupId);
            transactionGroupRepository.join(groupId, connection.getUniqueKey(), moduleName);
            this.result = "ok";
            protocoler.sendMsg(connection.getUniqueKey(), this);
        });
    }
}
