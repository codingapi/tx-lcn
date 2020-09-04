package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tm.id.SnowflakeInitiator;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.codingapi.txlcn.tm.constant.CommonConstant.TX_MANAGER;

/**
 * @author WhomHim
 * @description 启动连接，连接其他 TM 节点
 * @date Create in 2020/9/4 15:41
 */
@Slf4j
public class TmNodeServerRunner {

    @Value("${txlcn.protocol.port}")
    private int port;

    private ProtocolServer protocolServer;

    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private RedisTmNodeRepository redisTmNodeRepository;


    public TmNodeServerRunner(ProtocolServer protocolServer) {
        this.protocolServer = protocolServer;
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("tmNode-pool-%d").build());
    }

    /**
     * 初始化连接
     */
    public void init() {
        try {
            SnowflakeInitiator.SnowflakeVo snowflakeVo = SnowflakeInitiator.getSnowflakeVo();
            String id = String.format("%s_%d_%d", TX_MANAGER, snowflakeVo.getDataCenterId(), snowflakeVo.getWorkerId());
            String hostAddress = Objects.requireNonNull(NetUtil.getLocalhost()).getHostAddress();
            TmNode tmNode = new TmNode(id, hostAddress, port,redisTmNodeRepository);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                tmNode.connectToOtherNode(protocolServer);
            }, 0, 30, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
