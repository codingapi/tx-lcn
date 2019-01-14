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
package com.codingapi.txlcn.client.core.txc.resource.rs;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.FieldValue;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.ModifiedRecord;
import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 适配修改记录的查询结果
 * <p>
 * Date: 2018/12/13
 *
 * @author ujued
 */
public class UpdateSqlPreDataHandler implements ResultSetHandler<List<ModifiedRecord>> {

    private final List<String> columns;
    private final List<String> primaryKeys;

    public UpdateSqlPreDataHandler(List<String> primaryKeys, List<String> columns) {
        this.columns = columns;
        this.primaryKeys = primaryKeys;
    }

    @Override
    public List<ModifiedRecord> handle(ResultSet rs) throws SQLException {
        List<ModifiedRecord> modifiedRecords = new ArrayList<>();
        while (rs.next()) {
            ModifiedRecord record = SqlUtils.recordByColumns(rs, columns);

            for (String primaryKey : primaryKeys) {
                FieldValue fieldValue = new FieldValue();
                fieldValue.setTableName(SqlUtils.tableName(primaryKey));
                fieldValue.setFieldName(primaryKey);
                fieldValue.setValue(rs.getObject(primaryKey));
                fieldValue.setValueType(fieldValue.getValue().getClass());
                record.getFieldClusters().get(fieldValue.getTableName()).getPrimaryKeys().add(fieldValue);
            }
            modifiedRecords.add(record);
        }
        return modifiedRecords;
    }
}
