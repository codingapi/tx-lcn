package com.codingapi.txlcn.tc.core.txc.resource.def.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-18 下午5:38
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertImageParams {
    private String groupId;
    private String unitId;

    /**
     * Table Name
     */
    private String tableName;

    /**
     * Auto Increment PK Values
     */
    private ResultSet resultSet;

    private List<Map<String, Object>> primaryKeyValuesList;

    private List<String> fullyQualifiedPrimaryKeys;

}
