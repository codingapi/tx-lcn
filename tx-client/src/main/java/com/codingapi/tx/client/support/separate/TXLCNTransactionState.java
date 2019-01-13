package com.codingapi.tx.client.support.separate;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
public enum TXLCNTransactionState {

    /**
     * 发起事务状态
     */
    STARTING("starting"),

    /**
     * 参与中事务状态
     */
    RUNNING("running"),

    /**
     * 默认事务
     */
    DEFAULT("default"),

    /**
     * 不参与分布式事务
     */
    NON("non");


    private String code;


    TXLCNTransactionState(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
