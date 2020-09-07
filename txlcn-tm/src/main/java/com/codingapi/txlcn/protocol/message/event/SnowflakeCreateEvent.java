package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;
import com.codingapi.txlcn.tm.id.SnowflakeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author WhomHim
 * @description Snowflake 生成事件
 * @date Create in 2020-8-14 22:13:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class SnowflakeCreateEvent extends SnowflakeMessage {

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 日志主键
     */
    private long logId;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        this.groupId = SnowflakeHandler.generateGroupId();
        this.logId = SnowflakeHandler.generateLogId();
        log.info("SnowflakeCreateEvent request msg =>{}", instanceId);
        protocoler.sendMsg(connection.getUniqueKey(), this);
        log.info("SnowflakeCreateEvent.send =>[groupId:{},logId:{}]", instanceId, logId);
    }
}
