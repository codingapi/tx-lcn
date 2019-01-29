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
package com.codingapi.txlcn.tm.support.db.mybatis;

import com.codingapi.txlcn.tm.support.db.domain.TxException;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Mapper
public interface TxExceptionMapper {

    @Insert("insert into t_tx_exception(group_id, unit_id, mod_id, transaction_state, registrar, ex_state, create_time) " +
            "values(#{groupId}, #{unitId}, #{modId}, #{transactionState}, #{registrar}, #{exState}, #{createTime})")
    void save(TxException txException);

    @Select("select * from t_tx_exception where group_id=#{groupId} and unit_id=#{unitId}")
    TxException getByGroupAndUnitId(@Param("groupId") String groupId, @Param("unitId") String unitId);

    @Select("select * from t_tx_exception")
    List<TxException> findAll();

    @Update("update t_tx_exception set ex_state=#{transactionState} where id=#{id}")
    void changeExState(@Param("id") Long id, @Param("transactionState") short state);

    @Select("select transaction_state from t_tx_exception where group_id=#{groupId} limit 1")
    Integer getTransactionStateByGroupId(String groupId);

    @Select("select * from t_tx_exception where ex_state=#{exState} and registrar=#{registrar}")
    List<TxException> findByExStateAndRegistrar(@Param("exState") Integer exState, @Param("registrar") Integer registrar);

    @Select("select * from t_tx_exception where ex_state=#{exState}")
    List<TxException> findByExState(Integer exState);

    @Select("select * from t_tx_exception")
    List<TxException> findByRegistrar(Integer registrar);

    @DeleteProvider(type = TxExceptionMapperProvider.class, method = "deleteByIdList")
    void deleteByIdList(List<Long> ids);
}
