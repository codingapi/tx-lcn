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
package com.codingapi.txlcn.client.core.txc.resource.def;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.LockableSelect;
import com.codingapi.txlcn.jdbcproxy.p6spy.common.StatementInformation;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.SQLException;

/**
 * Description: 程序业务SQL执行拦截器
 * Date: 2018/12/13
 *
 * @author ujued
 */
public interface SqlExecuteInterceptor {

    /**
     * 程序业务{@code update} 语句执行前植入事务操作
     *
     * @param update SQL
     * @throws SQLException 事务判断资源锁定、不支持此SQL时抛出
     */
    void preUpdate(Update update) throws SQLException;

    /**
     * 程序业务{@code delete} 语句执行前植入事务操作
     *
     * @param delete SQL
     * @throws SQLException 事务判断资源锁定、不支持此SQL时抛出
     */
    void preDelete(Delete delete) throws SQLException;

    /**
     * 程序业务{@code insert} 语句执行前植入事务操作
     *
     * @param insert SQL
     * @throws SQLException 不支持此SQL时抛出
     */
    void preInsert(Insert insert) throws SQLException;

    /**
     * 程序业务{@code insert} 语句执行后植入事务操作
     *
     * @param statementInformation SQL语句相关信息
     * @throws SQLException 不支持此SQL时抛出
     */
    void postInsert(StatementInformation statementInformation) throws SQLException;

    /**
     * 程序业务{@code select} 语句执行前植入事务操作
     *
     * @param lockableSelect SelectSQL解析后的对象
     * @throws SQLException 事务判断资源锁定、不支持此SQL时抛出
     */
    void preSelect(LockableSelect lockableSelect) throws SQLException;

}
