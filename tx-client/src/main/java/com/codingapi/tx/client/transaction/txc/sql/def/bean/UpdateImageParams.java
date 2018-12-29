package com.codingapi.tx.client.transaction.txc.sql.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateImageParams {
    private String groupId;
    private String unitId;
    private RollbackInfo rollbackInfo;
    private List<String> tables;
    private List<String> columns;
    private List<String> primaryKeys;
    private String whereSql;

    public UpdateImageParams setTables(List<String> tables) {
        this.tables = tables;
        return this;
    }

    public UpdateImageParams setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public UpdateImageParams setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        return this;
    }

    public UpdateImageParams setWhereSql(String whereSql) {
        this.whereSql = whereSql;
        return this;
    }

    public UpdateImageParams setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public UpdateImageParams setRollbackInfo(RollbackInfo rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
        return this;
    }

    public UpdateImageParams setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }
}
