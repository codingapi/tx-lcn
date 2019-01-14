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
package com.codingapi.txlcn.client.support;

import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import com.codingapi.txlcn.client.message.helper.RpcExecuteService;
import com.codingapi.txlcn.client.support.common.TransactionCleanService;
import com.codingapi.txlcn.spi.message.LCNCmdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Description: BeanName 获取工具类
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Component
@Slf4j
public class LCNTransactionBeanHelper {


    /**
     * transaction bean 名称格式
     * control_%s_%s
     * transaction:前缀 %s:事务类型（lcn,tcc,txc） %s:事务状态(starting,running)
     */
    private static final String CONTROL_BEAN_NAME_FORMAT = "control_%s_%s";


    /**
     * message bean 名称格式
     * rpc_%s_%s
     * message:前缀 %s:事务类型（lcn,tcc,txc） %s:事务业务(commit,rollback)
     */
    private static final String RPC_BEAN_NAME_FORMAT = "rpc_%s_%s";


    /**
     * transaction bean 名称格式
     * transaction_%s_%s
     * transaction:前缀 %s:事务类型（lcn,tcc,txc)
     */
    private static final String TRANSACTION_BEAN_NAME_FORMAT = "transaction_%s";

    /**
     * transaction state resolver
     * transaction_state_resolver_%s
     * %s:transaction type. lcn, tcc, txc so on.
     */
    private static final String TRANSACTION_STATE_RESOLVER_BEAN_NAME_FARMOT = "transaction_state_resolver_%s";

    /**
     * Transaction Clean Service
     * %s: transaction type
     */
    private static final String TRANSACTION_CLEAN_SERVICE_NAME_FORMAT = "%sTransactionCleanService";


    private final ApplicationContext spring;

    @Autowired
    public LCNTransactionBeanHelper(ApplicationContext spring) {
        this.spring = spring;
    }


    private String getControlBeanName(String transactionType, TXLCNTransactionState lcnTransactionState) {
        String name = String.format(CONTROL_BEAN_NAME_FORMAT, transactionType, lcnTransactionState.getCode());
        log.debug("getControlBeanName->{}", name);
        return name;
    }

    private String getRpcBeanName(String transactionType, LCNCmdType cmdType) {
        if (transactionType != null) {
            String name = String.format(RPC_BEAN_NAME_FORMAT, transactionType, cmdType.getCode());
            log.debug("getRpcBeanName->{}", name);
            return name;
        } else {
            String name = String.format(RPC_BEAN_NAME_FORMAT.replaceFirst("_%s", ""), cmdType.getCode());
            log.debug("getRpcBeanName->{}", name);
            return name;
        }
    }


    public TransactionResourceExecutor loadTransactionResourceExecuter(String beanName) {
        String name = String.format(TRANSACTION_BEAN_NAME_FORMAT, beanName);
        log.debug("loadTransactionResourceExecutor name ->{}", name);
        return spring.getBean(name, TransactionResourceExecutor.class);
    }


    private TXLCNTransactionControl loadLCNTransactionControl(String beanName) {
        return spring.getBean(beanName, TXLCNTransactionControl.class);
    }

    public TXLCNTransactionControl loadLCNTransactionControl(String transactionType, TXLCNTransactionState lcnTransactionState) {
        return loadLCNTransactionControl(getControlBeanName(transactionType, lcnTransactionState));
    }

    public RpcExecuteService loadRpcExecuteService(String transactionType, LCNCmdType cmdType) {
        return loadRpcExecuteService(getRpcBeanName(transactionType, cmdType));
    }

    private RpcExecuteService loadRpcExecuteService(String beanName) {
        return spring.getBean(beanName, RpcExecuteService.class);
    }

    /**
     * 获取事务状态决策器
     *
     * @param transactionType 事务类型
     * @return 事务状态决策器
     */
    public TXLCNTransactionSeparator loadLCNTransactionStateResolver(String transactionType) {
        try {
            String name = String.format(TRANSACTION_STATE_RESOLVER_BEAN_NAME_FARMOT, transactionType);
            return spring.getBean(name, TXLCNTransactionSeparator.class);
        } catch (Exception e) {
            return spring.getBean(String.format(TRANSACTION_STATE_RESOLVER_BEAN_NAME_FARMOT, "default"), TXLCNTransactionSeparator.class);
        }
    }


    public TransactionCleanService loadTransactionCleanService(String transactionType) {
        return spring.getBean(String.format(TRANSACTION_CLEAN_SERVICE_NAME_FORMAT, transactionType), TransactionCleanService.class);
    }
}
