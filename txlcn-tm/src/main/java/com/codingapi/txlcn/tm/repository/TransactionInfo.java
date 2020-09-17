package com.codingapi.txlcn.tm.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@Data
@NoArgsConstructor
public class TransactionInfo {

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 通讯唯一标示
     */
    private String uniqueKey;

    /**
     * 记录时间
     */
    private long time;

    /**
     * 事务类型
     */
    private TransactionType transactionType;


    public TransactionInfo( String uniqueKey,String moduleName,TransactionType transactionType) {
        this.uniqueKey = uniqueKey;
        this.moduleName = moduleName;
        this.transactionType = transactionType;
        this.time = System.currentTimeMillis();
    }

    public boolean hasJoin() {
        return TransactionType.JOIN.equals(transactionType);
    }


    public enum TransactionType{
        /**
         * 事务发起方
         */
        REQUEST,
        /**
         * 事务加入方
         */
        JOIN

    }
}
