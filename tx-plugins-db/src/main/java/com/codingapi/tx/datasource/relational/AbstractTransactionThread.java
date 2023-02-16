package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.aop.bean.TxTransactionLocal;
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
            logger.debug("start connection is wait ! ");
            return;
        }
        //标记已执行过该方法
        hasStartTransaction = true;
        Runnable runnable = new HookRunnable() {
            @Override
            public void run0() {
                //清空上下文对象
                TxTransactionLocal.setCurrent(null);
                try {
                    //执行提交或回滚。
                    transaction();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    try {
                        rollbackConnection();
                    } catch (SQLException e1) {
                        logger.error(e1.getMessage());
                    }
                } finally {
                    try {
                        //关闭回调函数，关闭task，从缓存中清除db连接，归还db连接。
                        closeConnection();
                    } catch (SQLException e) {
                        logger.error(e.getMessage());
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
