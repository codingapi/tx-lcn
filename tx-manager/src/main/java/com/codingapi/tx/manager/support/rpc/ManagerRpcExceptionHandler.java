package com.codingapi.tx.manager.support.rpc;

import com.codingapi.tx.spi.rpc.params.NotifyUnitParams;
import com.codingapi.tx.manager.core.service.TxExceptionService;
import com.codingapi.tx.manager.core.service.WriteTxExceptionDTO;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Component
@Slf4j
public class ManagerRpcExceptionHandler implements RpcExceptionHandler {

    private final TxExceptionService compensationService;

    private final RpcClient rpcClient;

    @Autowired
    public ManagerRpcExceptionHandler(TxExceptionService compensationService, RpcClient rpcClient) {
        this.compensationService = compensationService;
        this.rpcClient = rpcClient;
    }

    @Override
    public void handleNotifyUnitBusinessException(Object params, Throwable e) {
        // the same to rpc error
        handleNotifyUnitMessageException(params, e);
    }

    @Override
    public void handleNotifyUnitMessageException(Object params, Throwable e) {
        // notify unit rpc error, write txEx
        List paramList = ((List) params);
        try {
            String modName = rpcClient.getAppName((String) paramList.get(1));
            NotifyUnitParams notifyUnitParams = (NotifyUnitParams) paramList.get(0);
            WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO(notifyUnitParams.getGroupId(),
                    notifyUnitParams.getUnitId(), modName, (short) notifyUnitParams.getState());
            writeTxExceptionReq.setRegistrar((short) 0);
            compensationService.writeTxException(writeTxExceptionReq);
        } catch (RpcException e1) {
            log.error(e1.getMessage());
        }
    }
}
