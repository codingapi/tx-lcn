package com.codingapi.tx.datasource.relational.txc.parser;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caisirius
 */
public class TxcLine {

    private List<TxcField> fields = new ArrayList();

    // DiffUtils 比对时忽略此字段
    @JsonIgnore
    private Object primaryKey;

    @JsonIgnore
    private Object primaryValue;

    public List<TxcField> getFields() {
        return fields;
    }

    public void setFields(List<TxcField> fields) {
        this.fields = fields;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Object getPrimaryValue() {
        return primaryValue;
    }

    public void setPrimaryValue(Object primaryValue) {
        this.primaryValue = primaryValue;
    }
}