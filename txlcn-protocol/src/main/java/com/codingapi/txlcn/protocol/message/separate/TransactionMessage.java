package com.codingapi.txlcn.protocol.message.separate;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.await.Lock;
import com.codingapi.txlcn.protocol.await.LockContext;
import com.codingapi.txlcn.protocol.message.Connection;
import lombok.Data;
import org.springframework.context.ApplicationContext;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
public abstract class TransactionMessage extends AbsMessage {

    protected String groupId;

    protected String applicationName;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        //唤醒等待消息
        if(groupId!=null) {
            Lock lock = LockContext.getInstance().getKey(groupId);
            if (lock != null) {
                lock.setRes(this);
                lock.signal();
            }
        }
    }
}
