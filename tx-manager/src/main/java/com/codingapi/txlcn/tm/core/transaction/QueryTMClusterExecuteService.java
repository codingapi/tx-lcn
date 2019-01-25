package com.codingapi.txlcn.tm.core.transaction;

import com.codingapi.txlcn.commons.exception.FastStorageException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.tm.cluster.TMProperties;
import com.codingapi.txlcn.tm.core.message.RpcExecuteService;
import com.codingapi.txlcn.tm.core.message.TransactionCmd;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Description:
 * Date: 19-1-25 上午11:05
 *
 * @author ujued
 */
@Component("rpc_query-tm-cluster")
public class QueryTMClusterExecuteService implements RpcExecuteService {

    private final FastStorage fastStorage;

    @Autowired
    public QueryTMClusterExecuteService(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            HashSet<String> tmSet = new HashSet<>();
            for (TMProperties props : fastStorage.findTMProperties()) {
                tmSet.add(props.getHost() + ":" + props.getTransactionPort());
            }
            return tmSet;
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
