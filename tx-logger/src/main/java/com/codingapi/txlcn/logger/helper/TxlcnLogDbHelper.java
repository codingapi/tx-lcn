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

import com.codingapi.txlcn.logger.db.TxLog;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/17
 *
 * @author codingapi
 */
public interface TxlcnLogDbHelper {

    /**
     * 数据库初始化操作.
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 插入数据
     * @param txLoggerInfo logbean
     * @return rs
     */
     int insert(TxLog txLoggerInfo);

    /**
     * 分页获取记录
     *
     * @param left      分页开始
     * @param right     分页结束
     * @param timeOrder 时间排序SQL
     * @return 结果集
     */
     List<TxLog> findByLimit(int left, int right, int timeOrder) ;


    /**
     * GroupID 和 Tag 查询
     *
     * @param left      分页左侧
     * @param right     分页右侧
     * @param groupId   groupId
     * @param tag       标签
     * @param timeOrder timeOrder
     * @return 数据集
     */
     List<TxLog> findByGroupAndTag(int left, int right, String groupId, String tag, int timeOrder) ;



    /**
     * ag 查询
     *
     * @param left      分页左侧
     * @param right     分页右侧
     * @param tag       标签
     * @param timeOrder timeOrder
     * @return 数据集
     */
     List<TxLog> findByTag(int left, int right, String tag, int timeOrder) ;



    /**
     * GroupId 查询
     *
     * @param left      分页左侧
     * @param right     分页右侧
     * @param groupId   标签
     * @param timeOrder timeOrder
     * @return 数据集
     */
     List<TxLog> findByGroupId(int left, int right, String groupId, int timeOrder);




    /**
     * 分页获取记录所有记录数
     *
     * @return 总数
     */
     long findByLimitTotal() ;

    /**
     * GroupId 和 Tag 查询记录数
     *
     * @param groupId groupId
     * @param tag     标示
     * @return 数量
     */
     long findByGroupAndTagTotal(String groupId, String tag) ;


    /**
     * Tag 查询记录数
     *
     * @param tag 标示
     * @return 数量
     */
     long findByTagTotal(String tag) ;

    /**
     * GroupId 查询记录数
     *
     * @param groupId GroupId
     * @return 总数
     */
     long findByGroupIdTotal(String groupId);

}
