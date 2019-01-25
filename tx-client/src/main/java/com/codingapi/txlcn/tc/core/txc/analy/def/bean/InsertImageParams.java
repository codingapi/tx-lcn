package com.codingapi.txlcn.tc.core.txc.analy.def.bean;

import lombok.Data;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-18 下午5:38
 *
 * @author ujued
 */
@Data
public class InsertImageParams {

    /**
     * Auto Increment PK Values
     */
    private Statement statement;

    /**
     * Table Name
     */
    private String tableName;

    private List<Map<String, Object>> primaryKeyValuesList;

    private List<String> fullyQualifiedPrimaryKeys;
}
