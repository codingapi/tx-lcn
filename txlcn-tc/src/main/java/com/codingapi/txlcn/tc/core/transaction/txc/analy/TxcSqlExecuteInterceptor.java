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
package com.codingapi.txlcn.tc.core.transaction.txc.analy;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.SqlExecuteInterceptor;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.TxcService;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.*;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.util.SqlUtils;
import com.codingapi.txlcn.common.exception.TxcLogicException;
import com.codingapi.txlcn.tc.support.p6spy.common.StatementInformation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: 拦截必要的SQL, 植入TXC逻辑
 * <p>
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcSqlExecuteInterceptor implements SqlExecuteInterceptor {

    private final TableStructAnalyser tableStructAnalyser;

    private final TxcService txcService;

    private final TCGlobalContext globalContext;

    @Autowired
    public TxcSqlExecuteInterceptor(TableStructAnalyser tableStructAnalyser, TxcService txcService, TCGlobalContext globalContext) {
        this.tableStructAnalyser = tableStructAnalyser;
        this.txcService = txcService;
        this.globalContext = globalContext;
    }

    @Override
    public void preUpdate(Update update) throws SQLException {
        // 获取线程传递参数
        Connection connection = (Connection) DTXLocalContext.cur().getResource();

        // Update相关数据准备
        List<String> columns = new ArrayList<>(update.getColumns().size());
        List<String> primaryKeys = new ArrayList<>(3);
        List<String> tables = new ArrayList<>(update.getTables().size());
        update.getColumns().forEach(column -> {
            column.setTable(update.getTables().get(0));
            columns.add(column.getFullyQualifiedName());
        });
        for (Table table : update.getTables()) {
            tables.add(table.getName());
            TableStruct tableStruct = globalContext.tableStruct(table.getName(),
                    () -> tableStructAnalyser.analyse(connection, table.getName()));
            tableStruct.getPrimaryKeys().forEach(key -> primaryKeys.add(table.getName() + "." + key));
        }

        // 前置准备
        try {
            UpdateImageParams updateImageParams = new UpdateImageParams();
            updateImageParams.setColumns(columns);
            updateImageParams.setPrimaryKeys(primaryKeys);
            updateImageParams.setTables(tables);
            updateImageParams.setWhereSql(update.getWhere() == null ? "1=1" : update.getWhere().toString());
            txcService.resolveUpdateImage(updateImageParams);
        } catch (TxcLogicException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void preDelete(Delete delete) throws SQLException {
        // 获取线程传递参数
        Connection connection = (Connection) DTXLocalContext.cur().getResource();

        // 获取Sql Table
        if (delete.getTables().size() == 0) {
            delete.setTables(Collections.singletonList(delete.getTable()));
        }

        // Delete Sql 数据
        List<String> tables = new ArrayList<>(delete.getTables().size());
        List<String> primaryKeys = new ArrayList<>(3);
        List<String> columns = new ArrayList<>();

        for (Table table : delete.getTables()) {
            TableStruct tableStruct = globalContext.tableStruct(table.getName(),
                    () -> tableStructAnalyser.analyse(connection, table.getName()));
            tableStruct.getColumns().forEach((k, v) -> columns.add(tableStruct.getTableName() + SqlUtils.DOT + k));
            tableStruct.getPrimaryKeys().forEach(primaryKey -> primaryKeys.add(tableStruct.getTableName() + SqlUtils.DOT + primaryKey));
            tables.add(tableStruct.getTableName());
        }

        // 前置准备
        try {
            DeleteImageParams deleteImageParams = new DeleteImageParams();
            deleteImageParams.setColumns(columns);
            deleteImageParams.setPrimaryKeys(primaryKeys);
            deleteImageParams.setTables(tables);
            deleteImageParams.setSqlWhere(delete.getWhere().toString());
            txcService.resolveDeleteImage(deleteImageParams);
        } catch (TxcLogicException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void preInsert(Insert insert) {
    }

    @Override
    public void postInsert(StatementInformation statementInformation) throws SQLException {
        Connection connection = (Connection) DTXLocalContext.cur().getResource();
        Insert insert = (Insert) statementInformation.getAttachment();
        TableStruct tableStruct = globalContext.tableStruct(insert.getTable().getName(),
                () -> tableStructAnalyser.analyse(connection, insert.getTable().getName()));

        // 非自增主键值
        PrimaryKeyListVisitor primaryKeyListVisitor = new PrimaryKeyListVisitor(insert.getTable(),
                insert.getColumns(), tableStruct.getFullyQualifiedPrimaryKeys());
        insert.getItemsList().accept(primaryKeyListVisitor);

        try {
            InsertImageParams insertImageParams = new InsertImageParams();
            insertImageParams.setTableName(tableStruct.getTableName());
            insertImageParams.setStatement(statementInformation.getStatement());
            insertImageParams.setFullyQualifiedPrimaryKeys(tableStruct.getFullyQualifiedPrimaryKeys());
            insertImageParams.setPrimaryKeyValuesList(primaryKeyListVisitor.getPrimaryKeyValuesList());
            txcService.resolveInsertImage(insertImageParams);
        } catch (TxcLogicException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void preSelect(LockableSelect lockableSelect) throws SQLException {
        // 忽略无锁的查询
        if (!lockableSelect.shouldLock()) {
            return;
        }

        // 不支持非PlainSelect
        if (!(lockableSelect.statement().getSelectBody() instanceof PlainSelect)) {
            throw new SQLException("non support this query when use control lock.");
        }

        PlainSelect plainSelect = (PlainSelect) lockableSelect.statement().getSelectBody();

        // 不支持复杂的FromItem
        if (!(plainSelect.getFromItem() instanceof Table)) {
            throw new SQLException("non support this query when use control lock.");
        }


        // 构造查询需要判断锁行的SQL
        List<String> primaryKeys = new ArrayList<>();
        Table leftTable = (Table) plainSelect.getFromItem();
        List<SelectItem> selectItems = new ArrayList<>();
        Connection connection = (Connection) DTXLocalContext.cur().getResource();

        TableStruct leftTableStruct = globalContext.tableStruct(leftTable.getName(),
                () -> tableStructAnalyser.analyse(connection, leftTable.getName()));
        leftTableStruct.getPrimaryKeys().forEach(primaryKey -> {
            Column column = new Column(leftTable, primaryKey);
            selectItems.add(new SelectExpressionItem(column));
            primaryKeys.add(column.getFullyQualifiedName());
        });

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                if (join.isSimple()) {
                    TableStruct rightTableStruct = globalContext.tableStruct(join.getRightItem().toString(),
                            () -> tableStructAnalyser.analyse(connection, join.getRightItem().toString()));
                    rightTableStruct.getPrimaryKeys().forEach(primaryKey -> {
                        Column column = new Column((Table) join.getRightItem(), primaryKey);
                        selectItems.add(new SelectExpressionItem(column));
                        primaryKeys.add(column.getFullyQualifiedName());
                    });
                }
            }
        }
        plainSelect.setSelectItems(selectItems);

        // 尝试锁定
        log.info("lock select sql: {}", plainSelect);
        SelectImageParams selectImageParams = new SelectImageParams();
        selectImageParams.setPrimaryKeys(primaryKeys);
        selectImageParams.setSql(plainSelect.toString());

        try {
            txcService.lockSelect(selectImageParams, lockableSelect.isxLock());
        } catch (TxcLogicException e) {
            throw new SQLException(e.getMessage());
        }
    }

}
