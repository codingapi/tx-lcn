package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Description:
 * Date: 19-1-25 上午11:11
 *
 * @author ujued
 */
@Component("rpc_clean-invalid-tm")
@Slf4j
public class CleanInvalidTMExecuteService implements RpcExecuteService {

    private final FastStorage fastStorage;

    private final TxLogger txLogger;

    @Autowired
    public CleanInvalidTMExecuteService(FastStorage fastStorage, TxLogger txLogger) {
        this.fastStorage = fastStorage;
        this.txLogger = txLogger;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        HashSet hashSet = transactionCmd.getMsg().loadBean(HashSet.class);
        for (Object address : hashSet) {
            String[] args = ApplicationInformation.splitAddress(address.toString());
            try {
                fastStorage.removeTMProperties(args[0], Integer.valueOf(args[1]));
            } catch (FastStorageException e) {
                txLogger.trace("", "", Transactions.TE, "remove TM " + address + " fail.");
            }
        }
        log.info("Clean invalid TM: {}", hashSet);
        return null;
    }
}
