package com.codingapi.tx.spi.rpc;

import com.codingapi.tx.spi.rpc.dto.ManagerProperties;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public interface RpcServerInitializer {


    /**
     * support server init
     *
     * @param managerProperties   配置信息
     */
    void init(ManagerProperties managerProperties);

}
