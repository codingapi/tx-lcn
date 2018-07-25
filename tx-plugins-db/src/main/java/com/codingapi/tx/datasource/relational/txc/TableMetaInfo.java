package com.codingapi.tx.datasource.relational.txc;

import java.util.HashMap;
import java.util.Map;

/**
 * [表描述]
 *
 * @author caican
 * @date 17/12/23
 */
public class TableMetaInfo {
    /**
     * SchemaName (if empty, set to CatalogName)
     */
    private String schemaName;
    /**
     * TableName
     */
    private String tableName;

    /**
     * 列信息
     */
    private Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
    /**
     * 索引
     */
    private Map<String, IndexInfo> indexInfoMap = new HashMap<>();


    public TableMetaInfo() {
    }

    /**
     * @param name
     * @return
     */
    public ColumnInfo getColumnByName(String name) {
        String str = name.toUpperCase();
        ColumnInfo ret = this.columnInfoMap.get(str);
        if (ret == null) {
            if (name.charAt(0) == '`') {
                ret = this.columnInfoMap.get(str.substring(1, name.length() - 1));
            } else {
                ret = this.columnInfoMap.get("`" + str + "`");
            }
        }
        return ret;
    }


    public Map<String, ColumnInfo> getPrimaryKey() {
        HashMap<String, ColumnInfo> ret = new HashMap<>();

        for (Map.Entry<String, ColumnInfo> entry : this.columnInfoMap.entrySet()) {
            ColumnInfo columnInfo = entry.getValue();
            if (columnInfo.getKeyType() == 0) {
                ret.put(entry.getKey(), columnInfo);
            }
        }

        if (ret.size() > 1) {
            throw new RuntimeException("multi pks not support yet.");
        }
        return ret;
    }

    public String getPrimaryKeyName() {
        Map<String, ColumnInfo> primaryKey = getPrimaryKey();

        if (primaryKey.entrySet().size() > 1) {
            throw new RuntimeException("multi pks not support yet.");

        }

        Map.Entry<String, ColumnInfo> next = primaryKey.entrySet().iterator().next();
        return next.getKey();
    }

    public String getAutoIncrementPrimaryKey() {

        for (Map.Entry<String, ColumnInfo> entry : this.columnInfoMap.entrySet()) {
            ColumnInfo columnInfo = entry.getValue();
            if (columnInfo.getKeyType() == 0 && columnInfo.getExtra().equals("auto_increment")) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ColumnInfo> getColumnInfoMap() {
        return columnInfoMap;
    }

    public void setColumnInfoMap(Map<String, ColumnInfo> columnInfoMap) {
        this.columnInfoMap = columnInfoMap;
    }

    public Map<String, IndexInfo> getIndexInfoMap() {
        return indexInfoMap;
    }

    public void setIndexInfoMap(Map<String, IndexInfo> indexInfoMap) {
        this.indexInfoMap = indexInfoMap;
    }
}
