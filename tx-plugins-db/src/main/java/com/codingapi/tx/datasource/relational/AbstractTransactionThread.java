package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.framework.thread.HookRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * create by lorne on 2017/12/1
 */
public abstract class AbstractTransactionThread {

    private volatile boolean hasStartTransaction = false;

    private Logger logger = LoggerFactory.getLogger(AbstractTransactionThread.class);

    protected void startRunnable(){
        if(hasStartTransaction){
            logger.info("start connection is wait ! ");
            return;
        }
        hasStartTransaction = true;
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


    protected abstract void transaction() throws SQLException;

    protected abstract void closeConnection() throws SQLException;

    protected abstract void rollbackConnection() throws SQLException;
}
