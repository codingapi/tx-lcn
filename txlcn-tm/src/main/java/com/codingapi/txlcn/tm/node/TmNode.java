package com.codingapi.txlcn.tm.node;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codingapi.txlcn.tm.constant.CommonConstant.TX_MANAGE_KEY;

/**
 * @author WhomHim
 * @description TM 的 p2p 单节点
 * @date Create in 2020/9/3 17:22
 */
@Data
@Accessors(chain = true)
public class TmNode {

    /**
     * tm 的全局唯一 Id
     */
    private String id;

    private String nodeIp;

    private int port;

    private RedisTmNodeRepository redisTmNodeRepository;

    public TmNode(String hostAndPort, String nodeIp, int port, RedisTmNodeRepository redisTmNodeRepository) {
        this.id = hostAndPort;
        this.nodeIp = nodeIp;
        this.port = port;
        this.redisTmNodeRepository = redisTmNodeRepository;
    }

    private List<InetSocketAddress> getOtherNodeList() {
        List<String> otherNodeName = redisTmNodeRepository.keys(TX_MANAGE_KEY).stream()
                .filter(s -> !s.equals(id))
                .collect(Collectors.toList());
        return otherNodeName.stream()
                .map(tmKey -> redisTmNodeRepository.getTmNodeAddress(tmKey))
                .filter(s -> !s.equals(String.format("%s:%s", nodeIp, port)))
                .map(NetUtil::addressFormat)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public void connectToOtherNode(ProtocolServer protocolServer) {
        List<InetSocketAddress> otherNodeList = this.getOtherNodeList();
        otherNodeList.forEach(iNetSocketAddress ->
                protocolServer.connectTo(iNetSocketAddress.getHostString(), iNetSocketAddress.getPort()));

    }
}
