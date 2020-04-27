package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import lombok.Data;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Data
public class TransactionCreateEvent extends AbsMessage {

    /**
     * 事务Id
     */
    private Long groupId;


}
