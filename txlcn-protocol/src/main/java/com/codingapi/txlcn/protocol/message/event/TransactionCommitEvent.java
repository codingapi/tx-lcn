package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class TransactionCommitEvent extends TransactionMessage {

    private boolean commit;

    private String result;

    public TransactionCommitEvent(String groupId, boolean commit) {
        this.groupId = groupId;
        this.commit = commit;
    }
}
