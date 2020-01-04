package com.codingapi.txlcn.protocol;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author lorne
 * @date 2019/11/27
 * @description
 */
public class PeerEventLoopGroup {

    private final Config config;

    private final int portToBind;

    private final EventLoopGroup acceptorEventLoopGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup networkEventLoopGroup = new NioEventLoopGroup(6);

    private final EventLoopGroup peerEventLoopGroup = new NioEventLoopGroup(1);

    private final ObjectEncoder encoder = new ObjectEncoder();

    public PeerEventLoopGroup(String peerName,int port){
        this(new Config(peerName),port);
    }

    private PeerEventLoopGroup(Config config, int portToBind) {
        this.config = config;
        this.portToBind = portToBind;
    }

    public Config getConfig() {
        return config;
    }

    public int getPortToBind() {
        return portToBind;
    }

    public EventLoopGroup getAcceptorEventLoopGroup() {
        return acceptorEventLoopGroup;
    }

    public EventLoopGroup getNetworkEventLoopGroup() {
        return networkEventLoopGroup;
    }

    public EventLoopGroup getPeerEventLoopGroup() {
        return peerEventLoopGroup;
    }

    public ObjectEncoder getEncoder() {
        return encoder;
    }
}
