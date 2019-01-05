package com.codingapi.tx.spi.rpc.netty;


import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.dto.RpcCmd;
import com.codingapi.tx.spi.rpc.dto.RpcResponseState;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import com.codingapi.tx.spi.rpc.netty.bean.NettyRpcCmd;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lorne on 2017/6/30.
 */
@Slf4j
public class SocketManager {

    private final AttributeKey<String> attributeKey = AttributeKey.valueOf(SocketManager.class.getName());

    private ChannelGroup channels;

    private static SocketManager manager = null;

    public static SocketManager getInstance() {
        if (manager == null) {
            synchronized (SocketManager.class) {
                if (manager == null) {
                    manager = new SocketManager();
                }
            }
        }
        return manager;
    }


    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }


    private SocketManager() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    public int channelSize(){
        return channels.size();
    }



    private Channel getChannel(String key) throws RpcException {
        Iterator<Channel> iterator = channels.iterator();
        while (iterator.hasNext()) {
            Channel channel = iterator.next();
            String val = channel.remoteAddress().toString();
            if (key.equals(val)) {
                return channel;
            }
        }
        throw new RpcException("channel not online.");
    }


    public RpcResponseState send(String key, RpcCmd cmd) throws RpcException {
        Channel channel = getChannel(key);
        ChannelFuture future = channel.writeAndFlush(cmd).syncUninterruptibly();
        return future.isSuccess() ? RpcResponseState.success : RpcResponseState.fail;
    }

    public MessageDto request(String key, RpcCmd cmd) throws RpcException {
        NettyRpcCmd nettyRpcCmd = (NettyRpcCmd) cmd;
        log.debug("get channel, key:{}", key);
        Channel channel = getChannel(key);
        log.debug("write and flush sync");
        channel.writeAndFlush(nettyRpcCmd);
        log.debug("await response");
        nettyRpcCmd.await();
        MessageDto res = cmd.loadResult();
        nettyRpcCmd.loadRpcContent().clear();
        return res;
    }



    public List<String> loadAllRemoteKey(){
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
            allKeys.add(channel.remoteAddress().toString());
        }
        return allKeys;
    }


    public ChannelGroup getChannels() {
        return channels;
    }

    public int currentSize() {
        return channels.size();
    }


    public boolean noConnect(SocketAddress socketAddress) {
        for (Channel channel : channels) {
            if(channel.remoteAddress().toString().equals(socketAddress.toString())){
                return false;
            }
        }
        return true;
    }

    public List<String> moduleList(String moduleName) {
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
             if(getModuleName(channel).equals(moduleName)){
                 allKeys.add(channel.remoteAddress().toString());
             }
        }
        return allKeys;
    }


    public void bindModuleName(String remoteKey,String moduleName) throws RpcException{
       Channel channel = getChannel(remoteKey);
       Attribute<String> attribute =  channel.attr(attributeKey);
       attribute.set(moduleName);
    }

    public String getModuleName(Channel channel){
        Attribute<String> attribute =  channel.attr(attributeKey);
        return attribute.get();
    }

    public String getModuleName(String remoteKey)throws RpcException{
        Channel channel = getChannel(remoteKey);
        Attribute<String> attribute =  channel.attr(attributeKey);
        return attribute.get();
    }

}
