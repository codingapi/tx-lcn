package com.codingapi.tx.aop.bean;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
public class TxCompensateLocal {

    private final static ThreadLocal<TxCompensateLocal> currentLocal = new ThreadLocal<TxCompensateLocal>();

    private String groupId;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public static TxCompensateLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxCompensateLocal current) {
        currentLocal.set(current);
    }


}
