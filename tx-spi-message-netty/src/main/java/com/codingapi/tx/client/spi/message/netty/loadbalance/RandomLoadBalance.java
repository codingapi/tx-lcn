package com.codingapi.tx.client.spi.message.netty.loadbalance;

import com.codingapi.tx.client.spi.message.exception.RpcException;
import com.codingapi.tx.client.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.tx.client.spi.message.netty.SocketManager;
import io.netty.channel.Channel;

import java.util.Random;

/**
 * @author lorne
 * @date 2019/1/5
 * @description
 */
public class RandomLoadBalance implements RpcLoadBalance {

    private Random random;

    public RandomLoadBalance() {
        random = new Random();
    }

    @Override
    public String getRemoteKey() throws RpcException {
        int size = SocketManager.getInstance().currentSize();
        if (size == 0) {
            throw new RpcException(RpcException.NON_TX_MANAGER, "not can used connection");
        }
        int randomIndex = random.nextInt(size);
        int index = 0;
        for (Channel channel : SocketManager.getInstance().getChannels()) {
            if (index == randomIndex) {
                return channel.remoteAddress().toString();
            }
            index++;
        }
        throw new RpcException("channels was empty.");
    }
}
