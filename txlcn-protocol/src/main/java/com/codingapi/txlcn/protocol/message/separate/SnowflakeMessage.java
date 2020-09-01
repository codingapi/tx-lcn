package com.codingapi.txlcn.protocol.message.separate;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.await.Lock;
import com.codingapi.txlcn.protocol.await.LockContext;
import com.codingapi.txlcn.protocol.message.Connection;
import lombok.Data;
import org.springframework.context.ApplicationContext;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-8-16 14:23:54
 */
@Data
public abstract class SnowflakeMessage extends AbsMessage {

    protected String instanceId;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        //唤醒等待消息
        if (instanceId != null) {
            Lock lock = LockContext.getInstance().getKey(instanceId);
            if (lock != null) {
                lock.setRes(this);
                lock.signal();
            }
        }
    }
}
