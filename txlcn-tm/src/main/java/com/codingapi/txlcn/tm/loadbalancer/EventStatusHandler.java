package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.event.SnowflakeCreateEvent;
import com.codingapi.txlcn.protocol.message.event.TransactionCreateEvent;
import com.codingapi.txlcn.protocol.message.event.TransactionJoinEvent;
import com.codingapi.txlcn.protocol.message.event.TransactionNotifyEvent;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-12 22:43:52
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Slf4j
@Service
public class EventStatusHandler implements EventStatus {

    @Autowired
    private LoadBalancerInterceptor loadBalancerInterceptor;

    @Override
    public void onFirstNode(AbsMessage absMessage, String firstMessageIdTmp, Connection connection) {
        if (absMessage.getIsFirstNode()) {
            absMessage.setFirstMessageId(absMessage.getMessageId());
            log.debug("EventStatusHandler.onFirstNode ");

            AbsMessage tmMessage = this.getAbsMessage(absMessage);
            AbsMessage message = loadBalancerInterceptor.requestMsgToOtherTm(tmMessage, connection);

            BeanUtils.copyProperties(message, absMessage);
            absMessage.setMessageId(firstMessageIdTmp);
            absMessage.setIsFirstNode(true);
        }
    }

    @Override
    public void onBusinessExecuted(AbsMessage absMessage, EventService event) throws Exception {
        if (!absMessage.getIsFirstNode() && !absMessage.getIsBusinessExecuted()) {
            log.debug("EventStatusHandler.onBusinessExecuted");
            event.execute();
        }
    }

    @Override
    public void onReadyCallBack(AbsMessage absMessage, Protocoler protocoler, String firstConnectionKeyTmp) {
        if (absMessage.getIsFirstNode() && absMessage.getIsReadyCallBack()) {
            log.debug("EventStatusHandler.onReadyCallBack");
            log.debug("connection.getUniqueKey():{}", firstConnectionKeyTmp);
            protocoler.sendMsg(firstConnectionKeyTmp, absMessage);
            log.info("Tm event send back to Tc : {}", absMessage);
        }
    }

    @Override
    public void onOneConnection(EventService event) throws Exception {
        log.debug("EventStatusHandler.onOneConnection");
        event.execute();
    }

    /**
     * 判断 AbsMessage 是来自哪个事件的，并 new 一个该事件
     *
     * @param absMessage AbsMessage
     * @return Event
     */
    private AbsMessage getAbsMessage(AbsMessage absMessage) {
        AbsMessage tmMessage = null;
        if (absMessage instanceof SnowflakeCreateEvent) {
            tmMessage = new SnowflakeCreateEvent();
        }
        if (absMessage instanceof TransactionCreateEvent) {
            tmMessage = new TransactionCreateEvent();
        }
        if (absMessage instanceof TransactionJoinEvent) {
            tmMessage = new TransactionJoinEvent();
        }
        if (absMessage instanceof TransactionNotifyEvent) {
            tmMessage = new TransactionNotifyEvent();
        }
        return tmMessage;
    }
}
