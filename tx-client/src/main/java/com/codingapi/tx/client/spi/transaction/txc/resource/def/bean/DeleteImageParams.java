package com.codingapi.tx.client.spi.transaction.txc.resource.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/14
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class DeleteImageParams {
    private String groupId;
    private String unitId;
    private RollbackInfo rollbackInfo;
    private List<String> primaryKeys;
    private List<String> columns;
    private List<String> tables;
    private String sqlWhere;

    public DeleteImageParams setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        return this;
    }

    public DeleteImageParams setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public DeleteImageParams setTables(List<String> tables) {
        this.tables = tables;
        return this;
    }

    public DeleteImageParams setSqlWhere(String sqlWhere) {
        this.sqlWhere = sqlWhere;
        return this;
    }

    public DeleteImageParams setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public DeleteImageParams setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }

    public DeleteImageParams setRollbackInfo(RollbackInfo rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
        return this;
    }
}
