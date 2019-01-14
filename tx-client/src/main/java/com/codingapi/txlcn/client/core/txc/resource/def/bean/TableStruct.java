package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 描述数据表结构
 * <p>
 * Date: 2018/12/11
 *
 * @author ujued
 */
@AllArgsConstructor
@Data
public class TableStruct {

    public TableStruct(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 主键列表(非全限定名)
     */
    private List<String> primaryKeys = new ArrayList<>();

    /**
     * 主键列表(全限定名)
     */
    private List<String> fullyQualifiedPrimaryKeys;

    /**
     * 所有字段及类型（包括主键）
     */
    private Map<String, String> columns = new HashMap<>();

    /**
     * 重写Getter
     *
     * @return
     */
    public List<String> getFullyQualifiedPrimaryKeys() {
        if (this.fullyQualifiedPrimaryKeys != null) {
            return this.fullyQualifiedPrimaryKeys;
        }
        List<String> pks = new ArrayList<>();
        this.getPrimaryKeys().forEach(key -> pks.add(tableName + '.' + key));
        this.fullyQualifiedPrimaryKeys = pks;
        return this.fullyQualifiedPrimaryKeys;
    }
}
