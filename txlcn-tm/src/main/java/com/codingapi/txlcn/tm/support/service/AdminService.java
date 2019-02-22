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
import com.codingapi.txlcn.tm.support.restapi.vo.*;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
public interface AdminService {

    /**
     * 登陆
     *
     * @param password password
     * @return token
     * @throws TxManagerException TxManagerException
     */
    String login(String password) throws TxManagerException;

    /**
     * 查询TX 日志
     *
     * @param page      page
     * @param limit     limit
     * @param groupId   groupId
     * @param tag       tag
     * @param lTime     startTime
     * @param rTime     stopTime
     * @param timeOrder 时间排序1 顺序 2 逆序
     * @return TxLogList
     * @throws TxManagerException TxManagerException
     */
    TxLogList txLogList(Integer page, Integer limit, String groupId, String tag, String lTime, String rTime, Integer timeOrder) throws TxManagerException;

    /**
     * 分布式事务统计信息
     *
     * @return DTXInfo
     */
    DTXInfo dtxInfo();

    /**
     * 获取TxManager信息
     *
     * @return TxManagerInfo
     */
    TxManagerInfo getTxManagerInfo();

    /**
     * 删除日志
     *
     * @param deleteLogsReq deleteLogsReq
     * @throws TxManagerException TxManagerException
     */
    void deleteLogs(DeleteLogsReq deleteLogsReq) throws TxManagerException;

    /**
     * AppMods
     *
     * @param page  page
     * @param limit limit
     * @return AppMods
     */
    ListAppMods listAppMods(Integer page, Integer limit);

}
