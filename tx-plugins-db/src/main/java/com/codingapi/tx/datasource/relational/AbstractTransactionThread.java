package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.framework.thread.HookRunnable;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * create by lorne on 2017/12/1
 */
public abstract class AbstractTransactionThread {


    protected void startRunnable(){
        Runnable runnable = new HookRunnable() {
            @Override
            public void run0() {
                try {
                    transaction();
                } catch (Exception e) {
                    try {
                        rollbackConnection();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    try {
                        closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    protected void setAutoCommitMethod(boolean autoCommit, Logger logger, Connection connection) throws SQLException {
        if(!autoCommit) {
            logger.info("setAutoCommit - >" + autoCommit);
            connection.setAutoCommit(autoCommit);

            TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
            txTransactionLocal.setAutoCommit(autoCommit);
        }
    }


    protected abstract void transaction() throws SQLException;

    protected abstract void closeConnection() throws SQLException;

    protected abstract void rollbackConnection() throws SQLException;
}
