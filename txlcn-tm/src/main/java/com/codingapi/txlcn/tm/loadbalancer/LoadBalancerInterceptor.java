package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.reporter.TmManagerReporter;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/9 18:06
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Slf4j
@Service
public class LoadBalancerInterceptor implements EventInterceptor {

    @Autowired
    private TmManagerReporter tmManagerReporter;

    @Autowired
    private TmNodeRepository tmNodeRepository;

    @Autowired
    private TmConfig tmConfig;

    @Autowired
    private EventStatus eventStatus;

    @Override
    public void handle(AbsMessage absMessage, Protocoler protocoler, Connection connection, EventService event)
            throws Exception {
        String firstConnectionKeyTmp = connection.getUniqueKey();
        String firstMessageIdTmp = absMessage.getMessageId();
        if (protocoler.getConnections().size() == 1) {
            eventStatus.onOneConnection(event);
        } else {
            eventStatus.onFirstNode(absMessage, firstMessageIdTmp, connection);
            eventStatus.onBusinessExecuted(absMessage, event);
            eventStatus.onReadyCallBack(absMessage, protocoler, firstConnectionKeyTmp);
        }

    }

    /**
     * 发送消息到其他 TM
     *
     * @param absMessage AbsMessage
     * @param connection 第一个 TM 节点的连接信息
     * @return AbsMessage
     */
    public AbsMessage requestMsgToOtherTm(AbsMessage absMessage, Connection connection) {
        log.debug("=> LoadBalancerInterceptor requestMsgToOtherTm");
        String hostAddress = Objects.requireNonNull(NetUtil.getLocalhost()).getHostAddress();
        String tmId = String.format("%s:%s", hostAddress, tmConfig.getPort());
        TmNode tmNode = new TmNode(tmId, hostAddress, tmConfig.getPort(), tmNodeRepository);
        // 其他 node 节点
        List<InetSocketAddress> otherNodeList = tmNode.getOtherNodeList();

        return tmManagerReporter.requestMsg(absMessage, connection, otherNodeList);
    }

}