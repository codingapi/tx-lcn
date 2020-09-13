package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;

/**
 * @author WhomHim
 * @description 事件的流转状态
 * @date Create in 2020-9-12 22:38:04
 */
public interface EventStatus {

    /**
     * 事件状态在第一个 TM 节点
     *
     * @param absMessage        AbsMessage
     * @param firstMessageIdTmp 暂时保存 messageId
     * @param connection        Connection
     */
    void onFirstNode(AbsMessage absMessage, String firstMessageIdTmp, Connection connection);

    /**
     * 事件状态在除了第一个 TM 节点的其他节点上，并在执行事件的业务逻辑
     *
     * @param absMessage AbsMessage
     * @param event      EventService
     */
    void onBusinessExecuted(AbsMessage absMessage, EventService event) throws Exception;

    /**
     * 事件状态在准备发送结果的时候
     *
     * @param absMessage            AbsMessage
     * @param protocoler            protocoler
     * @param firstConnectionKeyTmp 暂时保存 ConnectionUniKey
     */
    void onReadyCallBack(AbsMessage absMessage, Protocoler protocoler, String firstConnectionKeyTmp);

    /**
     * TM 节点只连接了一个 TC 时
     *
     * @param event EventService
     */
    void onOneConnection(EventService event) throws Exception;
}
