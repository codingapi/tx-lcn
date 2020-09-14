package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.TmNodeMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * @author WhomHim
 * @description 每当 TC 连接上一个 TM ，TM 会将其他 TM 的 ip 地址信息告诉 TC
 * @date Create in 2020-9-6 19:36:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class OtherTmNodeEvent extends TmNodeMessage {

    /**
     * 其他 TM 的 ip 地址信息
     */
    private List<InetSocketAddress> otherNodeList;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        log.info("OtherTmNodeEvent request msg =>{}", messageId);
        super.handle(springContext, protocoler, connection);
        TmNodeRepository tmNodeRepository = springContext.getBean(TmNodeRepository.class);
        TmConfig tmConfig = springContext.getBean(TmConfig.class);
        String hostAddress = Objects.requireNonNull(NetUtil.getLocalhost()).getHostAddress();
        String tmId = String.format("%s:%s", hostAddress, tmConfig.getPort());
        TmNode tmNode = new TmNode(tmId, hostAddress, tmConfig.getPort(), tmNodeRepository);
        this.otherNodeList = tmNode.getBesidesNodeList(otherNodeList);
        protocoler.sendMsg(connection.getUniqueKey(), this);
        log.info("OtherTmNodeEvent.send =>[tmId:{}]", tmId);
    }
}
