package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Data
public class TransactionNotifyEvent extends TransactionMessage {

    private String result;

    /**
     * 业务执行结果
     */
    private boolean success;

    public TransactionNotifyEvent(String groupId,boolean success) {
        this.groupId = groupId;
        this.success = success;
    }

}
