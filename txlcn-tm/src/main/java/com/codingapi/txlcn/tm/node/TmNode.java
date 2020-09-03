package com.codingapi.txlcn.tm.node;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WhomHim
 * @description TM 的 p2p 单节点
 * @date Create in 2020/9/3 17:22
 */
@Data
public class TmNode {

    /**
     * tm 的全局唯一 Id
     */
    private String id;

    private String nodeIp;

    private String port;

    /**
     * 记录 p2p 节点
     */
    private ArrayList p2pList;

    @Autowired
    private RedisTmNodeRepository  redisTmNodeRepository;

    private final static String TX_MANAGE_KEY = "TxManager*";

    private void getOtherNodeList(){
        List<String> collect = redisTmNodeRepository.keys(TX_MANAGE_KEY).stream()
                .filter(s -> !s.equals(id))
                .collect(Collectors.toList());
    }

    private void connectToOtherNode(ProtocolServer protocolServer){
//        protocolServer.connectTo()
    }
}
