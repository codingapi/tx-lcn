package com.codingapi.txlcn.manager.core.transaction;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.spi.message.params.NotifyGroupParams;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.manager.core.context.DTXTransaction;
import com.codingapi.txlcn.manager.core.context.TransactionManager;
import com.codingapi.txlcn.manager.core.context.DTXTransactionContext;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_notify-group")
@Slf4j
public class NotifyGroupExecuteService implements RpcExecuteService {

    private final TxLogger txLogger;

    private final TransactionManager transactionManager;

    private final DTXTransactionContext transactionContext;

    @Autowired
    public NotifyGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager, DTXTransactionContext transactionContext) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
        this.transactionContext = transactionContext;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        DTXTransaction dtxTransaction = transactionContext.getTransaction(transactionCmd.getGroupId());
        try {
            // 解析参数
            NotifyGroupParams notifyGroupParams = transactionCmd.getMsg().loadData(NotifyGroupParams.class);
            log.debug("notify group params: {}", JSON.toJSONString(notifyGroupParams));

            // 系统日志
            txLogger.trace(
                    transactionCmd.getGroupId(), "",
                    Transactions.TAG_TRANSACTION, "notify group " + notifyGroupParams.getState());

            if (notifyGroupParams.getState() == 1) {
                transactionManager.commit(dtxTransaction);
                return null;
            } else if (notifyGroupParams.getState() == 0) {
                transactionManager.rollback(dtxTransaction);
                return null;
            }
            log.error("ignored transaction state:{}", notifyGroupParams.getState());
        } catch (SerializerException e) {
            throw new TxManagerException(e.getMessage());
        } finally {
            transactionManager.close(dtxTransaction);

            // 系统日志
            txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "notify group over");
        }
        return null;
    }
}
