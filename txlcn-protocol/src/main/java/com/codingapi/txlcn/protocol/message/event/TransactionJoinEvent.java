package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Data
public class TransactionJoinEvent extends TransactionMessage {

    private String result;

    public TransactionJoinEvent(String groupId) {
        this.groupId = groupId;
    }

}
