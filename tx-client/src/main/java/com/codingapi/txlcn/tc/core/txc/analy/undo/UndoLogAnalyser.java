package com.codingapi.txlcn.tc.core.txc.analy.undo;

import com.codingapi.txlcn.tc.core.txc.analy.def.bean.FieldCluster;
import com.codingapi.txlcn.tc.core.txc.analy.def.bean.FieldValue;
import com.codingapi.txlcn.tc.core.txc.analy.def.bean.StatementInfo;
import com.codingapi.txlcn.tc.core.txc.analy.util.SqlUtils;

/**
 * Description:
 * Date: 19-1-21 上午10:07
 *
 * @author ujued
 */
public class UndoLogAnalyser {

    public static StatementInfo update(TableRecord tableRecord) {
        String k = tableRecord.getTableName();
        FieldCluster v = tableRecord.getFieldCluster();
        Object[] params = new Object[v.getFields().size() + v.getPrimaryKeys().size()];

        StringBuilder rollbackSql = new StringBuilder()
                .append(SqlUtils.UPDATE)
                .append(k)
                .append(SqlUtils.SET);
        int index = 0;
        for (int i = 0; i < v.getFields().size(); i++, index++) {
            FieldValue fieldValue = v.getFields().get(i);
            rollbackSql.append(fieldValue.getFieldName())
                    .append("=?")
                    .append(SqlUtils.SQL_COMMA_SEPARATOR);
            params[index] = fieldValue.getValue();
        }
        SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, rollbackSql);
        rollbackSql.append(SqlUtils.WHERE);

        for (int i = 0; i < v.getPrimaryKeys().size(); i++, index++) {
            FieldValue fieldValue = v.getPrimaryKeys().get(i);
            rollbackSql.append(fieldValue.getFieldName())
                    .append("=?")
                    .append(SqlUtils.AND);
            params[index] = fieldValue.getValue();
        }
        SqlUtils.cutSuffix(SqlUtils.AND, rollbackSql);

        return new StatementInfo(rollbackSql.toString(), params);
    }


    public static StatementInfo delete(TableRecord tableRecord) {
        String k = tableRecord.getTableName();
        FieldCluster v = tableRecord.getFieldCluster();

        StringBuilder rollbackSql = new StringBuilder(SqlUtils.INSERT).append(k).append('(');
        StringBuilder values = new StringBuilder();
        Object[] params = new Object[v.getFields().size()];
        for (int i = 0; i < v.getFields().size(); i++) {
            FieldValue fieldValue = v.getFields().get(i);
            rollbackSql.append(fieldValue.getFieldName()).append(SqlUtils.SQL_COMMA_SEPARATOR);
            values.append("?, ");
            params[i] = fieldValue.getValue();
        }
        SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, rollbackSql);
        SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, values);
        rollbackSql.append(") values(").append(values).append(')');
        return new StatementInfo(rollbackSql.toString(), params);
    }

    public static StatementInfo insert(TableRecord tableRecord) {
        StringBuilder rollbackSql = new StringBuilder(SqlUtils.DELETE)
                .append(SqlUtils.FROM)
                .append(tableRecord.getTableName())
                .append(SqlUtils.WHERE);

        Object[] paramArray = new Object[tableRecord.getFieldCluster().getPrimaryKeys().size()];
        for (int i = 0; i < tableRecord.getFieldCluster().getPrimaryKeys().size(); i++) {
            FieldValue fieldValue = tableRecord.getFieldCluster().getPrimaryKeys().get(i);
            rollbackSql.append(fieldValue.getFieldName()).append("=?").append(SqlUtils.AND);
            paramArray[i] = fieldValue.getValue();
        }
        SqlUtils.cutSuffix(SqlUtils.AND, rollbackSql);
        return new StatementInfo(rollbackSql.toString(), paramArray);
    }
}
