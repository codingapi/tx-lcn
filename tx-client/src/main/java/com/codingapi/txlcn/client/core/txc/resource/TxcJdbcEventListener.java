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
import com.codingapi.txlcn.client.core.txc.resource.def.SqlExecuteInterceptor;
import com.codingapi.txlcn.client.core.txc.resource.def.bean.LockableSelect;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.StatementInformation;
import com.codingapi.txlcn.jdbcproxy.p6spy.event.SimpleJdbcEventListener;
import com.codingapi.txlcn.jdbcproxy.p6spy.util.TxcUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @author lorne
 */
@Slf4j
@Component
public class TxcJdbcEventListener extends SimpleJdbcEventListener {

    private final SqlExecuteInterceptor sqlExecuteInterceptor;


    @Autowired
    public TxcJdbcEventListener(SqlExecuteInterceptor sqlExecuteInterceptor) {
        this.sqlExecuteInterceptor = sqlExecuteInterceptor;
    }

    @Override
    public void onBeforeAnyExecute(StatementInformation statementInformation) throws SQLException {
        String sql = statementInformation.getSqlWithValues();

        // 忽略TxcSQL
        if (TxcUtils.isTxcSQL(sql)) {
            return;
        }

        // 当前业务链接
        DTXLocal.cur().setResource(statementInformation.getStatement().getConnection());

        // 拦截处理
        try {
            long startTime = System.currentTimeMillis();
            Statement statement = CCJSqlParserUtil.parse(sql);
            log.debug("statement > {}", statement);
            statementInformation.setAttachment(statement);
            if (statement instanceof Update) {
                sqlExecuteInterceptor.preUpdate((Update) statement);
            } else if (statement instanceof Delete) {
                sqlExecuteInterceptor.preDelete((Delete) statement);
            } else if (statement instanceof Insert) {
                sqlExecuteInterceptor.preInsert((Insert) statement);
            } else if (statement instanceof Select) {
                sqlExecuteInterceptor.preSelect(new LockableSelect((Select) statement));
            }
            log.debug("used time: {} ms", System.currentTimeMillis() - startTime);
        } catch (JSQLParserException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void onAfterExecute(PreparedStatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        if (statementInformation.getAttachment() instanceof Insert) {
            try {
                sqlExecuteInterceptor.postInsert(statementInformation);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public void onAfterExecute(StatementInformation statementInformation, long timeElapsedNanos, String sql, SQLException e) {
        if (statementInformation.getAttachment() instanceof Insert) {
            try {
                sqlExecuteInterceptor.postInsert(statementInformation);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    @Override
    public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
        if (statementInformation.getAttachment() instanceof Insert) {
            try {
                sqlExecuteInterceptor.postInsert(statementInformation);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }


    @Override
    public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
        if (statementInformation.getAttachment() instanceof Insert) {
            try {
                sqlExecuteInterceptor.postInsert(statementInformation);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
