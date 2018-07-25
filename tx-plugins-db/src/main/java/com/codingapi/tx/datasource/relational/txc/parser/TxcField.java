package com.codingapi.tx.datasource.relational.txc.parser;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author caisirius
 */
public class TxcField {
    private String name;

    // DiffUtils 比对时忽略此字段
    @JsonIgnore
    private int type;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSqlName() {
        return "`" + name + "`";
    }


    @Override
    public String toString() {
        return String.format("[%s,%s]", new Object[]{this.name, String.valueOf(this.value)});
    }
}