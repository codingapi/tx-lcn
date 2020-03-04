package com.codingapi.txlcn.protocol.message.separate;

import lombok.Data;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
public abstract class AbsTxCreateGroupMsg extends AbsMessage {

    protected String groupId;

}
