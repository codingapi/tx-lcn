package com.lorne.tx.compensate.model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description: .</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 本地恢复实体事务bean
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/7/12 10:02
 * @since JDK 1.8
 */
public class TransactionRecover implements Serializable {


    /**
     * 主键id
     */
    private String id;


    /**
     * 同模块唯一标示
     */
    private String unique;


    /**
     * 重试次数，
     */
    private int retriedCount = 0;

    /**
     * 创建时间
     */
    private Date createTime = new Date();


    /**
     * 创建时间
     */
    private Date lastTime = new Date();


    /**
     * 事务组id
     */
    private String groupId;

    /**
     * 任务id
     */
    private String taskId;


    /**
     * 事务执行方法
     */
    private TransactionInvocation invocation;

    /**
     * 数据状态
     * 0 : 未处理
     * 1 : 执行中
     * 2 : task任务扫描中
     */
    private int state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getRetriedCount() {
        return retriedCount;
    }

    public void setRetriedCount(int retriedCount) {
        this.retriedCount = retriedCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public TransactionInvocation getInvocation() {
        return invocation;
    }

    public void setInvocation(TransactionInvocation invocation) {
        this.invocation = invocation;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }
}
