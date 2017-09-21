package com.lorne.tx.db.relational;

import com.lorne.tx.bean.TxTransactionLocal;
import com.lorne.tx.db.ICallClose;
import com.lorne.tx.db.service.DataSourceService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * create by lorne on 2017/7/29
 */

public class LCNDBConnection extends AbstractDBConnection {


    public LCNDBConnection(Connection connection, DataSourceService dataSourceService, TxTransactionLocal transactionLocal, ICallClose<AbstractDBConnection> runnable) {
        super(connection, dataSourceService, transactionLocal, runnable);
    }

    @Override
    public void transaction() throws SQLException {
        if (waitTask == null) {
            connection.rollback();
            dataSourceService.deleteCompensates(getCompensateList());
            System.out.println("waitTask is null");
            return;
        }


        //start 结束就是全部事务的结束表示,考虑start挂掉的情况
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("自动回滚->" + getGroupId());
                dataSourceService.schedule(getGroupId(), getCompensateList(), waitTask);
            }
        }, getMaxOutTime());

        System.out.println("transaction-awaitTask->" + getGroupId());
        waitTask.awaitTask();

        timer.cancel();

        int rs = waitTask.getState();

        System.out.println("(" + getGroupId() + ")->单元事务（1：提交 0：回滚 -1：事务模块网络异常回滚 -2：事务模块超时异常回滚）:" + rs);

        if (rs == 1) {
            connection.commit();
        } else {
            connection.rollback();
        }
        if (rs != -100) {
            dataSourceService.deleteCompensates(getCompensateList());
        }
        waitTask.remove();

    }


}
