package com.codingapi.tx.client.transaction.txc.sql.rs;

import com.codingapi.tx.client.transaction.txc.sql.def.bean.FieldValue;
import com.codingapi.tx.client.transaction.txc.sql.def.bean.ModifiedRecord;
import com.codingapi.tx.client.transaction.txc.sql.util.SqlUtils;
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
