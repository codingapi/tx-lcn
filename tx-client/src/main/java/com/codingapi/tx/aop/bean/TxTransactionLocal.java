package com.codingapi.tx.aop.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

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
     * 是否单模块下多次业务调用
     */
    private boolean hasMoreService = false;

    private String kid;

    private String type;

    private boolean autoCommit = true;

    private Transactional transactional;

    private List<String> cachedModelList = new ArrayList<String>();


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

    public boolean isHasMoreService() {
        return hasMoreService;
    }

    public void setHasMoreService(boolean hasMoreService) {
        this.hasMoreService = hasMoreService;
    }

    public TxTransactionLocal() {

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
        currentLocal.set(current);
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
