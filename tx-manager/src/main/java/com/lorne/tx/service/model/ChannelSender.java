package com.lorne.tx.service.model;

import com.lorne.core.framework.utils.http.HttpUtils;
import com.lorne.tx.utils.SocketUtils;
import io.netty.channel.Channel;

/**
 * create by lorne on 2017/8/7
 */
public class ChannelSender {


    private Channel channel;

    private String address;

    private String modelName;

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void send(String msg){
        if(channel!=null){
            SocketUtils.sendMsg(channel,msg);
        }else{
            String url = String.format("http://%s/tx/manager/sendMsg",address);
            HttpUtils.post(url,"msg="+msg+"&model="+modelName);
        }

    }
}
