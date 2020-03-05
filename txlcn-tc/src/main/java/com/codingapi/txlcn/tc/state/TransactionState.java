package com.codingapi.txlcn.tc.state;

import lombok.Data;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Data
public class TransactionState {

    private String TransactionType;

    /**
     * 是否是传递过来的事务
     * true 是传递的事务
     * false 自行创建的事务
     */
    private boolean transmitTransaction;

}
