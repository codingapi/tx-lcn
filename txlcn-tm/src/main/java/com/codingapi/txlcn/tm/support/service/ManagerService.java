/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tm.support.service;

import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.txmsg.params.NotifyConnectParams;
import com.codingapi.txlcn.txmsg.exception.RpcException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
public interface ManagerService {

    /**
     * refresh connect
     *
     * @param notifyConnectParams notifyConnectParams
     * @return bool
     * @throws RpcException RpcException
     */
    boolean refresh(NotifyConnectParams notifyConnectParams) throws RpcException;

    /**
     * machine id
     *
     * @return int
     * @throws TxManagerException TxManagerException
     */
    long machineIdSync() throws TxManagerException;

    /**
     * refresh machine id
     *
     * @param machineId machineId
     * @throws TxManagerException TxManagerException
     */
    void refreshMachines(long... machineId) throws TxManagerException;
}
