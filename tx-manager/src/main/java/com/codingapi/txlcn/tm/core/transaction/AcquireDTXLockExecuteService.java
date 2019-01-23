package com.codingapi.txlcn.tm.core.transaction;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.tm.core.message.RpcExecuteService;
import com.codingapi.txlcn.tm.core.message.TransactionCmd;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.commons.exception.FastStorageException;
import com.codingapi.txlcn.spi.message.params.DTXLockParams;
import com.codingapi.txlcn.tm.core.storage.LockValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-22 上午10:44
 *
 * @author ujued
 */
@Component("rpc_acquire-dtx-lock")
public class AcquireDTXLockExecuteService implements RpcExecuteService {

    private final FastStorage fastStorage;

    @Autowired
    public AcquireDTXLockExecuteService(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        DTXLockParams dtxLockParams = transactionCmd.getMsg().loadBean(DTXLockParams.class);
        try {
            LockValue lockValue = new LockValue();
            lockValue.setGroupId(transactionCmd.getGroupId());
            lockValue.setLockType(dtxLockParams.getLockType());
            fastStorage.acquireLocks(dtxLockParams.getContextId(), dtxLockParams.getLocks(), lockValue);
            return true;
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
