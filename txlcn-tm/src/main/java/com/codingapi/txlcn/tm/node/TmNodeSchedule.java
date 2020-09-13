package com.codingapi.txlcn.tm.node;

import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.tm.id.SnowflakeInitiator;
import com.codingapi.txlcn.tm.reporter.TmManagerReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author WhomHim
 * @description 30秒重新设置过期时间一次，相当于心跳一样告诉其他 TM “我还存活”
 * @date Create in 2020-8-17 22:34:28
 */
@Component
public class TmNodeSchedule {

    @Autowired
    private SnowflakeInitiator snowflakeInitiator;

    @Autowired
    private TmManagerReporter tmManagerReporter;

    @Scheduled(cron = "0/30 * * * * ?")
    private void snowflakeInitiatorResetExpire() {
        Collection<Connection> connections = tmManagerReporter.getConnections();
        snowflakeInitiator.resetExpire();
    }


}
