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
package com.codingapi.txlcn.manager.support.service;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.support.restapi.model.DTXInfo;
import com.codingapi.txlcn.manager.support.restapi.model.TxLogList;
import com.codingapi.txlcn.manager.support.restapi.model.TxManagerInfo;

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
     * @return  token
     */
    String login(String password) throws TxManagerException;

    /**
     * 查询TX 日志
     *
     * @param page page
     * @param limit limit
     * @param  groupId groupId
     * @param tag tag
     * @param timeOrder 时间排序1 顺序 2 逆序
     * @return TxLogList
     */
    TxLogList txLogList(Integer page, Integer limit, String groupId, String tag, Integer timeOrder);

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
}
