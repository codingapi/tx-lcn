package com.codingapi.tx.manager.core.transaction;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.client.spi.message.params.NotifyGroupParams;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.core.GroupTransaction;
import com.codingapi.tx.manager.core.TransactionManager;
import com.codingapi.tx.manager.core.message.RpcExecuteService;
import com.codingapi.tx.manager.core.message.TransactionCmd;
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

    @Autowired
    public NotifyGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            // 解析参数
            NotifyGroupParams notifyGroupParams = transactionCmd.getMsg().loadData(NotifyGroupParams.class);
            log.debug("notify group params: {}", JSON.toJSONString(notifyGroupParams));

            txLogger.trace(
                    transactionCmd.getGroupId(), "",
                    Transactions.TAG_TRANSACTION, "notify group " + notifyGroupParams.getState());

            if (notifyGroupParams.getState() == 1) {
                transactionManager.commit(new GroupTransaction(transactionCmd.getGroupId()));
            } else if (notifyGroupParams.getState() == 0) {
                transactionManager.rollback(new GroupTransaction(transactionCmd.getGroupId()));
            }

            // ignore
        } catch (SerializerException e) {
            throw new TxManagerException(e.getMessage());
        } finally {
            transactionManager.close(new GroupTransaction(transactionCmd.getGroupId()));
            txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "notify group over");
        }
        return null;
    }
}
