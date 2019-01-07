package com.codingapi.tx.spi.message.netty.bean;

import com.codingapi.tx.commons.util.RandomUtils;
import com.codingapi.tx.spi.message.dto.MessageDto;
import com.codingapi.tx.spi.message.dto.RpcCmd;
import com.codingapi.tx.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Slf4j
public class NettyRpcCmd extends RpcCmd {

    private volatile transient RpcContent rpcContent;

    public String randomKey() {
        String key = RandomUtils.randomKey();
        if (RpcCmdContext.getInstance().hasKey(key)) {
            return randomKey();
        } else {
            rpcContent = RpcCmdContext.getInstance().addKey(key);
        }
        return key;
    }

    @Override
    public MessageDto loadResult() throws RpcException {
        MessageDto msg = rpcContent.getRes();
        if (msg == null) {
            throw new RpcException("request timeout.");
        }
        log.debug("got response. {} ", getKey());
        return msg;
    }

    public RpcContent loadRpcContent() {
        if (rpcContent == null) {
            rpcContent = RpcCmdContext.getInstance().getKey(getKey());
        }
        return rpcContent;
    }

    public void await() {
        if (Objects.nonNull(rpcContent.getRes())) {
            return;
        }
        rpcContent.await();
    }

}
