package com.codingapi.tx.datasource.relational.txc;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.relational.txc.parser.CommitInfo;
import com.codingapi.tx.datasource.relational.txc.rollback.TxcRollbackService;
import com.codingapi.tx.datasource.service.DataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * create by caisirius on 2017/11/28
 */

public class TxcDBConnection extends AbstractTxcConnection {
    private Logger logger = LoggerFactory.getLogger(TxcDBConnection.class);

    public TxcDBConnection(Connection connection, TxTransactionLocal txTransactionLocal,
                           DataSourceService dataSourceService,
                           TxcRollbackService txcRollbackService) {
        super(connection, txTransactionLocal, dataSourceService, txcRollbackService);
    }

    @Override
    public void transaction() {
        if (waitTask == null) {
            logger.warn("waitTask is null");
            return;
        }

        // start 结束就是全部事务的结束表示,考虑start挂掉的情况
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.warn("txc自动回滚->" + getGroupId());
                dataSourceService.schedule(getGroupId(), waitTask);
            }
        }, maxOutTime);

        logger.info("waiting for TxManager notify, groupId {}, timeout {}", getGroupId(), maxOutTime);
        waitTask.awaitTask();

        timer.cancel();

        int rs = waitTask.getState();

        logger.info("lcn txc transaction over, groupId {} and state is {}",getGroupId(),(rs==1?"commit":"rollback"));
        // 提交
        if (rs == 1) {
            // do nothing
        } else {
            try {
                rollbackConnection();
            } catch (Exception e) {
                logger.error("rollback error", e);
            }
        }

        waitTask.remove();
    }

    @Override
    protected void closeConnection() throws SQLException {

        if (waitTask != null) {
            if (!waitTask.isRemove()) {
                waitTask.remove();
            }
        }
    }

    @Override
    protected void rollbackConnection() throws SQLException {
        logger.info("doTxcRollback kid:{},context:{}", waitTask.getKey()
                , JSON.toJSONString(txcRuntimeContext));
        List<CommitInfo> commitInfos = txcRuntimeContext.getInfo();

        // 逆序回滚
        for (int i = commitInfos.size() - 1; i >= 0; i--) {
            txcRollbackService.rollback(commitInfos.get(i));
        }
    }

}
