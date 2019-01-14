package com.codingapi.txlcn.manager.support.service;

import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;
import com.codingapi.txlcn.spi.message.exception.RpcException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
public interface ManagerService {

    boolean refresh(NotifyConnectParams notifyConnectParams) throws RpcException;
}
