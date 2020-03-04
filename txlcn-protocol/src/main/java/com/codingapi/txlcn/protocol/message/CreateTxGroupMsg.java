package com.codingapi.txlcn.protocol.message;

import com.codingapi.txlcn.protocol.Protocoler;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
@Data
@NoArgsConstructor
public class CreateTxGroupMsg implements Message {

    public CreateTxGroupMsg(String groupId) {
        this.groupId = groupId;
    }

    private String groupId;

    @Override
    public void handle(Protocoler protocoler, Connection connection) throws Exception {
        log.info("groupId:{}",groupId);
    }
}
