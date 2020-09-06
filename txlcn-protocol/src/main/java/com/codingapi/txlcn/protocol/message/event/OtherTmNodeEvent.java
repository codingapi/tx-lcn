package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-7 00:08:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OtherTmNodeEvent extends AbsMessage {
    private List<InetSocketAddress> otherNodeList;
}
