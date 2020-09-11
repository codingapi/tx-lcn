package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/9 17:48
 */
@FunctionalInterface
public interface EventInterceptor {

    SnowflakeMessage intercept(SnowflakeMessage absMessage, Connection connection);
}
