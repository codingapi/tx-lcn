package com.codingapi.txlcn.tc.aspect.weave;

import java.sql.Connection;

/**
 * Description:
 * Date: 19-1-30 上午9:23
 *
 * @author ujued
 */
public interface ConnectionCallback {

    /**
     * call proto getConnection
     *
     * @return java.sql.Connection
     * @throws Throwable ex
     */
    Connection call() throws Throwable;
}
