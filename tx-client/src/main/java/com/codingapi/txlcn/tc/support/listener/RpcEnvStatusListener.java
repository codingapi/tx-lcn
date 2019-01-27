package com.codingapi.txlcn.tc.support.listener;

/**
 * Description:
 * Date: 1/27/19
 *
 * @author ujued
 */
public interface RpcEnvStatusListener {
    void onConnected(String remoteKey);

    void onInitialized(String remoteKey);

    void onConnectFail(String remoteKey);
}
