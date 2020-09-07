package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.TmNodeMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-7 00:08:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OtherTmNodeEvent extends TmNodeMessage {
    private List<InetSocketAddress> otherNodeList;
}
