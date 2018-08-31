package com.codingapi.lcn.tx.bean;

import com.codingapi.lcn.tx.annotation.TxTransactionMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TxTransactionInfo {

    /**
     * 事务模式
     */
    private TxTransactionMode mode;

    /**
     * 切面信息
     */
    private TransactionInvocation invocation;


}
