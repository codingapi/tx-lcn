package com.codingapi.txlcn.tc.jdbc.log;

import com.codingapi.txlcn.tc.cache.Cache;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.Getter;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class TransactionLog {

    public TransactionLog(String sql) {
        this.id = Cache.getLogId();
        this.sql = sql;
        this.groupId = TransactionInfo.current().getGroupId();
        this.time = System.currentTimeMillis();
        this.flag = 0;
    }

    /**
     * 日志主键
     */
    @Getter
    private long id;

    /**
     * 事务组Id
     */
    private String groupId;

    /**
     * 日志sql
     */
    private String sql;

    /**
     * 执行时间
     */
    private long time;

    /**
     * 日志标示
     * 0  业务执行的sql
     * 1  框架添加的sql
     */
    private int flag;

    public Object[] params() {
        Object[] arrays = new Object[]{id,groupId,sql,time,flag};
        return arrays;
    }
}
