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
package com.codingapi.txlcn.client.core.txc.resource;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.TableStruct;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Description: 数据库表结构分析
 * <p>
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Component
public class TableStructAnalyser {

    private final DataSource dataSource;

    @Autowired
    public TableStructAnalyser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TableStruct analyse(Connection connection, String table) throws SQLException {
        ResultSet structRs = null;
        ResultSet columnSet = null;
        TableStruct tableStruct = new TableStruct(table);
        try {
            structRs = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), null, table);
            columnSet = connection.getMetaData().getColumns(null, "%", table, "%");
            while (structRs.next()) {
                tableStruct.getPrimaryKeys().add(structRs.getString("COLUMN_NAME"));
            }
            while (columnSet.next()) {
                tableStruct.getColumns().put(columnSet.getString("COLUMN_NAME"), columnSet.getString("TYPE_NAME"));
            }
        } catch (SQLException e) {
            try {
                DbUtils.close(structRs);
                DbUtils.close(columnSet);
            } catch (SQLException ignored) {
            }
            throw e;
        }
        return tableStruct;
    }

    public TableStruct analyse(String table) throws SQLException {
        Connection connection = null;
        try {
            DTXLocal.makeUnProxy();
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            return analyse(connection, table);
        } finally {
            DTXLocal.undoProxyStatus();
            DbUtils.close(connection);
        }
    }


    public boolean existsTable(Connection connection, String table) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = connection.getMetaData().getTables(null, null, table, null);
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DbUtils.close(resultSet);
        }
        return false;
    }

    /**
     * 存在数据表判断
     *
     * @param tableName tableName
     * @return exists
     * @throws  SQLException SQLException
     */
    public boolean existsTable(String tableName) throws SQLException {
        Connection connection = null;
        try {
            DTXLocal.makeUnProxy();
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            return existsTable(connection, tableName);
        } finally {
            DbUtils.close(connection);
            DTXLocal.undoProxyStatus();
        }
    }
}
