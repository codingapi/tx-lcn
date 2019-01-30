package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.cluster.TMProperties;
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
 * Date: 19-1-25 上午11:05
 *
 * @author ujued
 */
@Component("rpc_query-tm-cluster")
@Slf4j
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
            log.info("Query TM cluster. {}", tmSet);
            return tmSet;
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
