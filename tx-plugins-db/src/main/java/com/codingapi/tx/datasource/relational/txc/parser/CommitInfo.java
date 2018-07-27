package com.codingapi.tx.datasource.relational.txc.parser;

import com.google.common.collect.Lists;

import java.util.List;


public class CommitInfo implements Cloneable {
    /**
     * 更新之前 的行数据
     */
    private TxcTable originalValue = new TxcTable();
    /**
     * 更新后的行数据
     */
    private TxcTable presentValue = new TxcTable();

    /**
     * Where 条件
     */
    private String where = "";

    private List<Object> whereParams  = Lists.newArrayList();
    /**
     * 更新类型 UPDTAE or DELETE or insert
     */
    private SQLType sqlType = null;
    /**
     * 业务执行sql
     */
    private String sql = "";

    /**
     * sql的属性值,对应PreparedStatement存在
     */
    private List<Object> sqlParams = Lists.newArrayList();

    private String schemaName;



    public TxcTable getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(TxcTable originalValue) {
        this.originalValue = originalValue;
    }

    public TxcTable getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(TxcTable presentValue) {
        this.presentValue = presentValue;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public List<Object> getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(List<Object> whereParams) {
        this.whereParams = whereParams;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getSqlParams() {
        return sqlParams;
    }

    public void setSqlParams(List<Object> sqlParams) {
        this.sqlParams = sqlParams;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
