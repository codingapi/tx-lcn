package com.codingapi.txlcn.tm.core.transaction;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.tm.core.message.RpcExecuteService;
import com.codingapi.txlcn.tm.core.message.TransactionCmd;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-24 下午4:16
 *
 * @author ujued
 */
@Component("rpc_refresh-tm-cluster")
public class RefreshTMClusterExecuteService implements RpcExecuteService {

    private final ManagerService managerService;

    @Autowired
    public RefreshTMClusterExecuteService(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            return managerService.refresh(transactionCmd.getMsg().loadBean(NotifyConnectParams.class));
        } catch (RpcException e) {
            throw new TxManagerException(e);
        }
    }
}
