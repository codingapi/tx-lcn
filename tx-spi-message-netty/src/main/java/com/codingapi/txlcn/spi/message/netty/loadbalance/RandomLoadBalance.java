/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.spi.message.netty.loadbalance;

import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.txlcn.spi.message.netty.SocketManager;
import io.netty.channel.Channel;

import java.util.Random;

/**
 * @author lorne
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
