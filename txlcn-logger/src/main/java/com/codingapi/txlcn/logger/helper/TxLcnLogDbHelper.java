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
package com.codingapi.txlcn.logger.helper;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.logger.db.TxLog;
import com.codingapi.txlcn.logger.exception.TxLoggerException;
import com.codingapi.txlcn.logger.model.Field;
import com.codingapi.txlcn.logger.model.LogList;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/17
 *
 * @author codingapi
 */
public interface TxLcnLogDbHelper extends TxLcnInitializer {
    
    /**
     * 插入数据
     *
     * @param txLoggerInfo log bean
     * @return rs
     */
    int insert(TxLog txLoggerInfo);

    /**
     * 按字段删除日志
     *
     * @param fields 按给定字段筛选并删除记录
     * @throws TxLoggerException TxLoggerException
     */
    void deleteByFields(List<Field> fields) throws TxLoggerException;

    /**
     * 查找日志
     *
     * @param page page
     * @param limit limit
     * @param list list
     * @param timeOrder timeOrder
     * @return logs
     * @throws TxLoggerException TxLoggerException
     */
    LogList findByLimitAndFields(int page, int limit, int timeOrder, List<Field> list) throws TxLoggerException;
}
