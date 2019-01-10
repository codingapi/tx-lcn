package com.codingapi.tx.client.spi.transaction.txc.resource;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.SqlExecuteInterceptor;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.LockableSelect;
import com.codingapi.tx.client.spi.transaction.txc.resource.init.TxcSettingFactory;
import com.codingapi.tx.jdbcproxy.p6spy.common.PreparedStatementInformation;
import com.codingapi.tx.jdbcproxy.p6spy.common.StatementInformation;
import com.codingapi.tx.jdbcproxy.p6spy.event.SimpleJdbcEventListener;
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
 * @date 2018/12/3
 * @description
 */
@Slf4j
@Component
public class TxcJdbcEventListener extends SimpleJdbcEventListener {

    private final SqlExecuteInterceptor sqlExecuteInterceptor;

    private final TxcSettingFactory txcSettingFactory;

    @Autowired
    public TxcJdbcEventListener(SqlExecuteInterceptor sqlExecuteInterceptor, TxcSettingFactory txcSettingFactory) {
        this.sqlExecuteInterceptor = sqlExecuteInterceptor;
        this.txcSettingFactory = txcSettingFactory;
    }

    @Override
    public void onBeforeAnyExecute(StatementInformation statementInformation) throws SQLException {
        String sql = statementInformation.getSqlWithValues();

        // 忽略Txc数据表
        if (sql.contains(txcSettingFactory.lockTableName()) || sql.contains(txcSettingFactory.undoLogTableName())) {
            return;
        }

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
            e.printStackTrace();
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
