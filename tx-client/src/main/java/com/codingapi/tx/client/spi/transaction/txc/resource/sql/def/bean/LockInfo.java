package com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 行锁信息
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LockInfo {
    private String groupId;

    private String unitId;

    /**
     * 数据表
     */
    private String tableName;
    /**
     * 数据表内行标识
     */
    private String keyValue;

    /**
     * 是否是排他锁。为{@code false}时标识此锁为共享锁
     */
    private boolean xLock;

    public LockInfo setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public LockInfo setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }

    public LockInfo setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public LockInfo setKeyValue(String keyValue) {
        this.keyValue = keyValue;
        return this;
    }

    public LockInfo setxLock(boolean xLock) {
        this.xLock = xLock;
        return this;
    }
}
