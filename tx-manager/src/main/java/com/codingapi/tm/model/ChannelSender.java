package com.codingapi.tm.model;

import com.codingapi.tm.framework.utils.SocketUtils;
import com.lorne.core.framework.utils.http.HttpUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;

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
        }

    }

    public void send(String msg,Task task){
        if(channel!=null){
            SocketUtils.sendMsg(channel,msg);
        }else{
            String url = String.format("http://%s/tx/manager/sendMsg",address);
            final String res = HttpUtils.post(url,"msg="+msg+"&model="+modelName);
            if(StringUtils.isNotEmpty(res)){
                if(task!=null) {
                    task.setBack(new IBack() {
                        @Override
                        public Object doing(Object... objs) throws Throwable {
                            return res;
                        }
                    });
                    task.signalTask();
                }
            }else{
                if(task!=null) {
                    task.setBack(new IBack() {
                        @Override
                        public Object doing(Object... objs) throws Throwable {
                            return "-2";
                        }
                    });
                    task.signalTask();
                }
            }
        }

    }
}
