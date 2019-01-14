/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.client.core.txc.resource.util;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.FieldCluster;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.FieldValue;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.ModifiedRecord;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
public class SqlUtils {

    public static final String SQL_COMMA_SEPARATOR = ", ";

    public static final String DOT = ".";

    public static final String AND = " and ";

    public static final String OR = " or ";

    public static final String UPDATE = "UPDATE ";

    public static final String INSERT = "INSERT INTO ";

    public static final String DELETE = "DELETE ";

    public static final String SELECT = "SELECT ";

    public static final String FROM = " FROM ";

    public static final String WHERE = " WHERE ";

    public static final String SET = " SET ";

    public static final String LOCK_TABLE = "txc_lock";

    public static final String UNDO_LOG_TABLE = "txc_undo_log";

    public static final int MYSQL_TABLE_NOT_EXISTS_CODE = 1146;

    public static final String FOR_UPDATE = "FOR UPDATE";

    public static final String LOCK_IN_SHARE_MODE = "LOCK IN SHARE MODE";


    /**
     * 从完全标识字段名获取表名
     *
     * @param fieldFullyQualifiedName fieldFullyQualifiedName
     * @return tableName
     */
    public static String tableName(String fieldFullyQualifiedName) {
        if (fieldFullyQualifiedName.contains(".")) {
            return fieldFullyQualifiedName.substring(0, fieldFullyQualifiedName.indexOf("."));
        }
        return null;
    }

    /**
     * 从字符串构造器剪掉结束符
     *
     * @param suffix suffix
     * @param stringBuilder stringBuilder
     */
    public static void cutSuffix(String suffix, StringBuilder stringBuilder) {
        if (stringBuilder.substring(stringBuilder.length() - suffix.length()).equals(suffix)) {
            stringBuilder.delete(stringBuilder.length() - suffix.length(), stringBuilder.length());
        }
    }

    /**
     * 获取修改记录
     *
     * @param rs rs
     * @param columns columns
     * @return ModifiedRecord
     * @throws SQLException SQLException
     */
    public static ModifiedRecord recordByColumns(ResultSet rs, List<String> columns) throws SQLException {
        ModifiedRecord record = new ModifiedRecord();
        for (String column : columns) {
            FieldValue fieldValue = new FieldValue();
            fieldValue.setFieldName(column);
            fieldValue.setTableName(SqlUtils.tableName(column));
            fieldValue.setValue(rs.getObject(column));
            fieldValue.setValueType(fieldValue.getValue().getClass());
            if (record.getFieldClusters().get(fieldValue.getTableName()) != null) {
                record.getFieldClusters().get(fieldValue.getTableName()).getFields().add(fieldValue);
            } else {
                FieldCluster fieldCluster = new FieldCluster();
                fieldCluster.getFields().add(fieldValue);
                record.getFieldClusters().put(fieldValue.getTableName(), fieldCluster);
            }
        }
        return record;
    }

    /**
     * java Object to bytes
     *
     * @param o o
     * @return byte[]
     */
    public static byte[] objectToBlob(Object o) {
        try {
            return SerializerContext.getInstance().serialize(o);
        } catch (SerializerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * bytes to java object
     *
     * @param blob blob
     * @param type type
     * @param <T> T
     * @return T
     */
    public static <T> T blobToObject(byte[] blob, Class<T> type) {
        try {
            return SerializerContext.getInstance().deSerialize(blob, type);
        } catch (SerializerException e) {
            throw new RuntimeException(e);
        }
    }
}
