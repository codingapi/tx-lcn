package com.codingapi.tx.manager.core.service;

import com.codingapi.tx.client.springcloud.spi.message.params.NotifyConnectParams;
import com.codingapi.tx.client.springcloud.spi.message.exception.RpcException;

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
