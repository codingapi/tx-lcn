package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.SnowFlakeMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-8-15 00:49:37
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SnowFlakeCreateEvent extends SnowFlakeMessage {

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 日志主键
     */
    private long logId;



}
