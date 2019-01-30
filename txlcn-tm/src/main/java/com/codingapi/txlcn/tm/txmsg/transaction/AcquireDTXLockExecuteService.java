package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.txmsg.params.DTXLockParams;
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
            lockValue.setGroupId(dtxLockParams.getGroupId());
            lockValue.setLockType(dtxLockParams.getLockType());
            fastStorage.acquireLocks(dtxLockParams.getContextId(), dtxLockParams.getLocks(), lockValue);
            return true;
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
