package com.codingapi.tx.aop.bean;

import com.codingapi.tx.Constants;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
public class TxTransactionLocal {

    private final static ThreadLocal<TxTransactionLocal> currentLocal = new ThreadLocal<TxTransactionLocal>();

    private String groupId;

    private int maxTimeOut;

    /**
     * 是否同一个模块被多次请求
     */
    private boolean hasIsGroup = false;

    /**
     * 是否是发起方模块
     */
    private boolean hasStart = false;

    /**
     * 时候已经获取到连接对象
     */
    private boolean hasConnection = false;


    private String kid;

    private String type;

    private boolean readOnly = false;

    public boolean isHasIsGroup() {
        return hasIsGroup;
    }

    public void setHasIsGroup(boolean hasIsGroup) {
        this.hasIsGroup = hasIsGroup;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public boolean isHasStart() {
        return hasStart;
    }

    public void setHasStart(boolean hasStart) {
        this.hasStart = hasStart;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isHasConnection() {
        return hasConnection;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }


    public int getMaxTimeOut() {
        return maxTimeOut;
    }

    public void setMaxTimeOut(int maxTimeOut) {
        this.maxTimeOut = maxTimeOut;
    }


    public static TxTransactionLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxTransactionLocal current) {
        //删除缓存的数据
        if(current==null){
            TxTransactionLocal old =  currentLocal.get();
            if(old!=null) {
                String keyPrefix = old.getGroupId();
                for(String key:Constants.cacheModelInfo.keySet()){
                    if(key.startsWith(keyPrefix)){
                        Constants.cacheModelInfo.remove(key);
                    }
                }
            }
        }
        currentLocal.set(current);
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
