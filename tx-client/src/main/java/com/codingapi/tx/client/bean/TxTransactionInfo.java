package com.codingapi.tx.client.bean;


import com.codingapi.tx.client.aspect.transaction.BusinessSupplier;
import com.codingapi.tx.commons.bean.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
@Data
@AllArgsConstructor
public class TxTransactionInfo {

    private String transactionType;

    /**
     * 事务发起方
     */
    private boolean transactionStart;

    /**
     * 事务组标识
     */
    private String groupId;

    /**
     * 事务单元标识
     */
    private String unitId;

    /**
     * 事务切面信息
     */
    private TransactionInfo transactionInfo;

    /**
     * 业务执行器
     */
    private BusinessSupplier businessSupplier;

    /**
     * 切点方法
     */
    private Method pointMethod;

}

