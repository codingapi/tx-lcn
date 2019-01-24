package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.NotifyConnectParams;

import java.util.List;

/**
 * Description:
 * Date: 19-1-24 下午4:32
 *
 * @author ujued
 */
public interface ClusterMessenger {

    void queryTMInfoPacket(String tmKey) throws RpcException;

    void refreshTMCluster(String tmKey, NotifyConnectParams notifyConnectParams) throws RpcException;

    String tmRpcKeyByModId(String modId) throws RpcException;
}
