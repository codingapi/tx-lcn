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
package com.codingapi.txlcn.client.support.checking;

import com.codingapi.txlcn.client.message.helper.TxMangerReporter;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.spi.message.params.JoinGroupParams;
import com.codingapi.txlcn.spi.message.params.NotifyGroupParams;
import com.codingapi.txlcn.spi.message.params.TxExceptionParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 分布式事务异常处理器类
 * Date: 2018/12/20
 *
 * @author ujued
 * @see DTXExceptionHandler
 */
@Component
@Slf4j
public class DefaultDTXExceptionHandler implements DTXExceptionHandler {

    private final TransactionCleanTemplate transactionCleanTemplate;

    @Autowired
    private TxMangerReporter txMangerReporter;

    @Autowired
    public DefaultDTXExceptionHandler(TransactionCleanTemplate transactionCleanTemplate) {
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    @Override
    public void handleCreateGroupBusinessException(Object params, Throwable ex) throws BeforeBusinessException {
        throw new BeforeBusinessException(ex);
    }

    @Override
    public void handleCreateGroupMessageException(Object params, Throwable ex) throws BeforeBusinessException {
        throw new BeforeBusinessException(ex);
    }

    @Override
    public void handleJoinGroupBusinessException(Object params, Throwable ex) throws TxClientException {
        JoinGroupParams joinGroupParams = (JoinGroupParams) params;
        try {
            transactionCleanTemplate.clean(joinGroupParams.getGroupId(), joinGroupParams.getUnitId(), joinGroupParams.getUnitType(), 0);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error.", joinGroupParams.getUnitType());
        }
        throw new TxClientException(ex);
    }

    @Override
    public void handleJoinGroupMessageException(Object params, Throwable ex) throws TxClientException {
        throw new TxClientException(ex);
    }

    @Override
    public void handleNotifyGroupBusinessException(Object params, Throwable ex) {
        List paramList = (List) params;
        NotifyGroupParams notifyGroupParams = (NotifyGroupParams) paramList.get(0);
        String unitId = (String) paramList.get(1);
        String transactionType = (String) paramList.get(2);

        // 结束事务
        try {
            transactionCleanTemplate.clean(notifyGroupParams.getGroupId(), unitId, transactionType, notifyGroupParams.getState());
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error.", transactionType);
        }
    }

    @Override
    public void handleNotifyGroupMessageException(Object params, Throwable ex) {
        // 当0 时候
        List paramList = (List) params;
        NotifyGroupParams notifyGroupParams = (NotifyGroupParams) paramList.get(0);
        if (notifyGroupParams.getState() == 0) {
            handleNotifyGroupBusinessException(params, ex);
            return;
        }

        // 按状态正常结束事务（切面补偿记录将保留）
        // TxManager 存在请求异常或者响应异常两种情况。当请求异常时这里的业务需要补偿，当响应异常的时候需要做状态的事务清理。
        // 请求异常时
        //     参与放会根据上报补偿记录做事务的提交。
        // 响应异常时
        //     参与反会正常提交事务，本地业务提示事务。

        // 该两种情况下补偿信息均可以忽略,可直接把本地补偿记录数据删除。


        String unitId = (String) paramList.get(1);
        String transactionType = (String) paramList.get(2);
        try {
            transactionCleanTemplate.compensationClean(notifyGroupParams.getGroupId(), unitId, transactionType, notifyGroupParams.getState());
        } catch (TransactionClearException e) {
            log.error("{} > compensationClean transaction error.", transactionType);
        }

        // 上报Manager，上报直到成功.
        txMangerReporter.reportTransactionState(notifyGroupParams.getGroupId(), null,
                TxExceptionParams.NOTIFY_GROUP_ERROR, notifyGroupParams.getState());
    }
}
