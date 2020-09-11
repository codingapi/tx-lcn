package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.event.SnowflakeCreateEvent;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.reporter.TxManagerReporter;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/9 18:06
 */
@Slf4j
@Service
public class LoadBalancerInterceptor implements EventInterceptor {

    @Autowired
    private TxManagerReporter txManagerReporter;

    @Autowired
    private RedisTmNodeRepository redisTmNodeRepository;

    @Autowired
    private TmConfig tmConfig;

    @Override
    public SnowflakeCreateEvent intercept(SnowflakeMessage absMessage, Connection connection) {
        log.info("=> LoadBalancerInterceptor intercept");

        InetAddress localhost = NetUtil.getLocalhost();
        String hostAddress = Objects.requireNonNull(localhost).getHostAddress();
        String tmId = String.format("%s:%s", hostAddress, tmConfig.getPort());
        TmNode tmNode = new TmNode(tmId, hostAddress, tmConfig.getPort(), redisTmNodeRepository);

        List<InetSocketAddress> otherNodeList = tmNode.getOtherNodeList();

//        return tmManagerReporter.requestMsg(absMessage, connection,otherNodeList);
       return (SnowflakeCreateEvent) txManagerReporter.requestMsg(absMessage,connection,otherNodeList);
    }
}
