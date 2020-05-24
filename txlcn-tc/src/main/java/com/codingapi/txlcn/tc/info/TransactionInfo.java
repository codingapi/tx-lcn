package com.codingapi.txlcn.tc.info;

import lombok.Data;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Data
public class TransactionInfo {

    /**
     * 事务类型
     */
    private String transactionType;

    /**
     * 是否是传递过来的事务
     * true 是传递的事务
     * false 自行创建的事务
     */
    private boolean transmitTransaction;


}
