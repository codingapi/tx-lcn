package com.lorne.tx.utils;

import com.lorne.tx.Constants;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lorne on 2017/6/30.
 */
public class SocketManager {

    /**
     * 最大连接数
     */
    private int maxConnection = Constants.maxConnection;

    /**
     * 当前连接数
     */
    private int nowConnection;

    /**
     * 允许连接请求 true允许 false拒绝
     */
    private boolean allowConnection = true;

    private List<Channel> clients = null;

    private Map<String,String> lines = null;

    private static SocketManager manager = null;

    public static SocketManager getInstance() {
        if (manager == null){
            synchronized (SocketManager.class){
                if(manager==null){
                    manager = new SocketManager();
                }
            }
        }
        return manager;
    }


    public Channel getChannelByModelName(String name) {
        for (Channel channel : clients) {
            String modelName = channel.remoteAddress().toString();

            if (modelName.equals(name)) {
                return channel;
            }
        }
        return null;
    }

    private SocketManager() {
        clients = new CopyOnWriteArrayList<Channel>();
        lines = new ConcurrentHashMap<>();
    }

    public void addClient(Channel client) {
        clients.add(client);
        nowConnection = clients.size();

        allowConnection = (maxConnection != nowConnection);
    }

    public void removeClient(Channel client) {
        clients.remove(client);
        nowConnection = clients.size();

        allowConnection = (maxConnection != nowConnection);
    }


    public int getMaxConnection() {
        return maxConnection;
    }

    public int getNowConnection() {
        return nowConnection;
    }

    public boolean isAllowConnection() {
        return allowConnection;
    }

    public void outLine(String modelName) {
        lines.remove(modelName);
    }

    public void onLine(String modelName, String uniqueKey) {
        lines.put(modelName,uniqueKey);
    }

    public Channel getChannelByUniqueKey(String uniqueKey) {
        for (Channel channel : clients) {
            String modelName = channel.remoteAddress().toString();
            String value  = lines.get(modelName);
            if (uniqueKey.equals(value)) {
                return channel;
            }
        }
        return null;
    }
}
