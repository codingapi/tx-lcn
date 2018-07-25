package com.codingapi.tx.datasource.relational.txc.rollback;

import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/26.
 */
public abstract class AbstractRollback {
    private Logger logger = LoggerFactory.getLogger(AbstractRollback.class);


    public void rollback(CommitInfo commitInfo, Connection connection) throws SQLException {

        //check
        boolean flag = canRollback(commitInfo, connection);


        //rollback
        if (flag) {
            logger.info("rollback for sql:{}", commitInfo.getSql());
            List<PreparedStatement> preparedStatements = assembleRollbackSql(commitInfo, connection);

            for (PreparedStatement preparedStatement : preparedStatements) {
                preparedStatement.execute();
            }
            logger.info("rollback sql success");
        }
    }

    protected abstract List<PreparedStatement> assembleRollbackSql(CommitInfo commitInfo, Connection connection)
            throws SQLException;

    protected abstract boolean canRollback(CommitInfo commitInfo, Connection connection) throws SQLException;
}
