package com.codingapi.tx.datasource.relational.txc.rollback;

import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import com.codingapi.tx.datasource.relational.txc.parser.SQLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author jsy.
 * @title
 * @time 17/12/22.
 */
@Component
public class TxcRollbackServiceImpl implements TxcRollbackService {

    private Logger logger = LoggerFactory.getLogger(TxcRollbackServiceImpl.class);

    @Autowired
    private TxcRollbackDataSource rollbackDataSource;

    @Override
    public void rollback(CommitInfo commitInfo) {
        // 每次需要新获取一个连接
        Connection connection = null;
        try {
            connection = rollbackDataSource.getConnectionByDbName(commitInfo.getSchemaName());
            if (commitInfo.getSqlType() == SQLType.UPDATE) {
                UpdateRollback.getInstance().rollback(commitInfo, connection);
            }

            if (commitInfo.getSqlType() == SQLType.INSERT) {
                InsertRollback.getInstance().rollback(commitInfo, connection);
            }

            if (commitInfo.getSqlType() == SQLType.DELETE) {
                DeleteRollback.getInstance().rollback(commitInfo, connection);
            }
        } catch (Exception e) {
            logger.error("rollback error, sql:{}", commitInfo.getSql(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("close error", e);
            }
        }

    }
}
