package com.codingapi.tx.client.framework;

import com.codingapi.tx.client.framework.control.LCNTransactionControl;
import com.codingapi.tx.client.framework.control.LCNTransactionSeparator;
import com.codingapi.tx.client.framework.control.LCNTransactionState;
import com.codingapi.tx.client.framework.resouce.TransactionResourceExecutor;
import com.codingapi.tx.client.framework.rpc.RpcExecuteService;
import com.codingapi.tx.client.transaction.common.TransactionCleanService;
import com.codingapi.tx.commons.rpc.LCNCmdType;
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
     * control bean 名称格式
     * control_%s_%s
     * control:前缀 %s:事务类型（lcn,tcc,txc） %s:事务状态(starting,running)
     */
    private static final String CONTROL_BEAN_NAME_FORMAT = "control_%s_%s";


    /**
     * rpc bean 名称格式
     * rpc_%s_%s
     * rpc:前缀 %s:事务类型（lcn,tcc,txc） %s:事务业务(commit,rollback)
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

    public static void main(String[] args){
        System.out.println(new LCNTransactionBeanHelper(null).getRpcBeanName(null, LCNCmdType.createGroup));
    }

    @Autowired
    public LCNTransactionBeanHelper(ApplicationContext spring) {
        this.spring = spring;
    }


    private String getControlBeanName(String transactionType, LCNTransactionState lcnTransactionState) {
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


    private LCNTransactionControl loadLCNTransactionControl(String beanName) {
        return spring.getBean(beanName, LCNTransactionControl.class);
    }

    public LCNTransactionControl loadLCNTransactionControl(String transactionType, LCNTransactionState lcnTransactionState) {
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
    public LCNTransactionSeparator loadLCNTransactionStateResolver(String transactionType) {
        try {
            String name = String.format(TRANSACTION_STATE_RESOLVER_BEAN_NAME_FARMOT, transactionType);
            return spring.getBean(name, LCNTransactionSeparator.class);
        } catch (Exception e) {
            return spring.getBean(String.format(TRANSACTION_STATE_RESOLVER_BEAN_NAME_FARMOT, "default"), LCNTransactionSeparator.class);
        }
    }


    public TransactionCleanService loadTransactionCleanService(String transactionType) {
        return spring.getBean(String.format(TRANSACTION_CLEAN_SERVICE_NAME_FORMAT, transactionType), TransactionCleanService.class);
    }
}
