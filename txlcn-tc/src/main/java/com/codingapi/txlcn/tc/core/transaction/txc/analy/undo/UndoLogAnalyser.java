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
package com.codingapi.txlcn.tc.core.transaction.txc.analy.undo;

import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.StatementInfo;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.FieldCluster;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.FieldValue;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.util.SqlUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        int j = whereBuilder(v.getPrimaryKeys(), rollbackSql, params, index - 1);
        SqlUtils.cutSuffix(SqlUtils.AND, rollbackSql);
        return new StatementInfo(rollbackSql.toString(), Arrays.copyOf(params, j));
    }


    public static StatementInfo delete(TableRecord tableRecord) {
        String k = tableRecord.getTableName();
        FieldCluster v = tableRecord.getFieldCluster();

        StringBuilder rollbackSql = new StringBuilder(SqlUtils.INSERT).append(k).append('(');
        StringBuilder values = new StringBuilder();
        Object[] params = new Object[v.getFields().size()];
        int j = 0;
        for (int i = 0; i < v.getFields().size(); i++, j++) {
            FieldValue fieldValue = v.getFields().get(i);
            if (Objects.isNull(fieldValue.getValue())) {
                j--;
                continue;
            }
            params[i] = fieldValue.getValue();
            rollbackSql.append(fieldValue.getFieldName()).append(SqlUtils.SQL_COMMA_SEPARATOR);
            values.append("?, ");
        }
        SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, rollbackSql);
        SqlUtils.cutSuffix(SqlUtils.SQL_COMMA_SEPARATOR, values);
        rollbackSql.append(") values(").append(values).append(')');
        return new StatementInfo(rollbackSql.toString(), Arrays.copyOf(params, j + 1));
    }

    public static StatementInfo insert(TableRecord tableRecord) {
        StringBuilder rollbackSql = new StringBuilder(SqlUtils.DELETE)
                .append(SqlUtils.FROM)
                .append(tableRecord.getTableName())
                .append(SqlUtils.WHERE);
        Object[] paramArray = new Object[tableRecord.getFieldCluster().getPrimaryKeys().size()];
        int j = whereBuilder(tableRecord.getFieldCluster().getPrimaryKeys(), rollbackSql, paramArray, -1);
        SqlUtils.cutSuffix(SqlUtils.AND, rollbackSql);
        return new StatementInfo(rollbackSql.toString(), Arrays.copyOf(paramArray, j));
    }

    private static int whereBuilder(List<FieldValue> primaryKeys, StringBuilder sqlBuilder, Object[] params, int index) {
        int j = index;
        for (FieldValue fieldValue : primaryKeys) {
            j++;
            if (Objects.isNull(fieldValue.getValue())) {
                j--;
                continue;
            }
            sqlBuilder.append(fieldValue.getFieldName()).append("=?").append(SqlUtils.AND);
            params[j] = fieldValue.getValue();
        }
        return j + 1;
    }
}
