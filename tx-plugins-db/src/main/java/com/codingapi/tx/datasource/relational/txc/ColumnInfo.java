package com.codingapi.tx.datasource.relational.txc;

/**
 * [类描述]
 *
 * @author caican
 * @date 17/12/23
 */
public class ColumnInfo {
    private String tableName;
    private String columnName;
    private int type;
    /**
     * -1 : no key
     * 0: PRI 主键索引
     * 1: UNI 唯一索引
     * 2: MUL 普通索引（联合索引）
     */
    private int keyType;
    private boolean isAllowNull;
    private String defaultValue;
    private String extra;

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAllowNull() {
        return isAllowNull;
    }

    public void setAllowNull(boolean allowNull) {
        isAllowNull = allowNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
