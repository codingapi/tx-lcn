package com.lorne.tx.db.redis;

import com.lorne.tx.bean.TxTransactionLocal;
import com.lorne.tx.db.ICallClose;
import com.lorne.tx.db.service.DataSourceService;
import org.springframework.data.redis.connection.RedisConnection;

import java.util.Timer;
import java.util.TimerTask;

/**
 * create by lorne on 2017/8/22
 */
public class LCNRedisConnection  extends AbstractRedisConnection{


    public LCNRedisConnection(RedisConnection redisConnection, DataSourceService dataSourceService, TxTransactionLocal transactionLocal, ICallClose<AbstractRedisConnection> runnable) {
        super(redisConnection, dataSourceService, transactionLocal, runnable);
    }

    @Override
    public void transaction() throws Exception {
        if (waitTask == null) {
            redisConnection.close();
           // dataSourceService.deleteCompensates(getCompensateList());
            System.out.println("waitTask is null");
            return;
        }


        //start 结束就是全部事务的结束表示,考虑start挂掉的情况
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("自动回滚->" + getGroupId());
                dataSourceService.schedule(getGroupId(),  waitTask);
            }
        }, getMaxOutTime());

        System.out.println("transaction-awaitTask->" + getGroupId());
        waitTask.awaitTask();

        timer.cancel();

        int rs = waitTask.getState();

        System.out.println("(" + getGroupId() + ")->单元事务（1：提交 0：回滚 -1：事务模块网络异常回滚 -2：事务模块超时异常回滚）:" + rs);

        if (rs == 1) {
            redisConnection.exec();
        }
//        if (rs != -100) {
//            dataSourceService.deleteCompensates(getCompensateList());
//        }
        waitTask.remove();

    }
}
