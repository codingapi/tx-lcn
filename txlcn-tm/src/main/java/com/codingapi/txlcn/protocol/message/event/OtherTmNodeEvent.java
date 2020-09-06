package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
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
public class OtherTmNodeEvent extends AbsMessage {

    /**
     * 其他 TM 的 ip 地址信息
     */
    private List<InetSocketAddress> otherNodeList;

    /**
     * 当前 TM 的端口
     */
    @Value("${txlcn.protocol.port}")
    private int port;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        RedisTmNodeRepository redisTmNodeRepository = springContext.getBean(RedisTmNodeRepository.class);
        InetAddress localhost = NetUtil.getLocalhost();
        String hostAddress = Objects.requireNonNull(localhost).getHostAddress();
        String tmId = String.format("%s:%s", hostAddress, port);
        TmNode tmNode = new TmNode(tmId, hostAddress, port, redisTmNodeRepository);
        this.otherNodeList = tmNode.getOtherNodeList();
        protocoler.sendMsg(connection.getUniqueKey(), this);
        log.info("OtherTmNodeEvent.send =>[tmId:{}]", tmId);
    }
}
