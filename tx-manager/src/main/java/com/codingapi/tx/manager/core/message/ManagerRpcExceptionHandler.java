package com.codingapi.tx.manager.core.message;

import com.codingapi.tx.manager.support.service.TxExceptionService;
import com.codingapi.tx.manager.support.service.WriteTxExceptionDTO;
import com.codingapi.tx.client.spi.message.RpcClient;
import com.codingapi.tx.client.spi.message.params.NotifyUnitParams;
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
        // the same to message error
        handleNotifyUnitMessageException(params, e);
    }

    @Override
    public void handleNotifyUnitMessageException(Object params, Throwable e) {
        // notify unit message error, write txEx
        List paramList = ((List) params);
        String modName =  rpcClient.getAppName((String) paramList.get(1));

        NotifyUnitParams notifyUnitParams = (NotifyUnitParams) paramList.get(0);
        WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO(notifyUnitParams.getGroupId(),
                notifyUnitParams.getUnitId(), modName, (short) notifyUnitParams.getState());
        writeTxExceptionReq.setRegistrar((short) 0);
        compensationService.writeTxException(writeTxExceptionReq);
    }
}
