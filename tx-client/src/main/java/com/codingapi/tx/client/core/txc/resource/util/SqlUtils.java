package com.codingapi.tx.client.core.txc.resource.util;

import com.codingapi.tx.client.core.txc.resource.def.bean.FieldCluster;
import com.codingapi.tx.client.core.txc.resource.def.bean.FieldValue;
import com.codingapi.tx.client.core.txc.resource.def.bean.ModifiedRecord;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.util.serializer.SerializerContext;

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
     * @param fieldFullyQualifiedName
     * @return
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
     * @param suffix
     * @param stringBuilder
     */
    public static void cutSuffix(String suffix, StringBuilder stringBuilder) {
        if (stringBuilder.substring(stringBuilder.length() - suffix.length()).equals(suffix)) {
            stringBuilder.delete(stringBuilder.length() - suffix.length(), stringBuilder.length());
        }
    }

    /**
     * 获取修改记录
     *
     * @param rs
     * @param columns
     * @return
     * @throws SQLException
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
     * @param o
     * @return
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
     * @param blob
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T blobToObject(byte[] blob, Class<T> type) {
        try {
            return SerializerContext.getInstance().deSerialize(blob, type);
        } catch (SerializerException e) {
            throw new RuntimeException(e);
        }
    }
}
