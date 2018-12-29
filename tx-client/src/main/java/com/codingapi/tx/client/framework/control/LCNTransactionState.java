package com.codingapi.tx.client.framework.control;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
public enum LCNTransactionState {

    /**
     * 发起事务状态
     */
    STARTING("starting"),

    /**
     * 参与中事务状态
     */
    RUNNING("running"),

    /**
     * 不参与分布式事务
     */
    DEFAULT("default");


    private String code;


    LCNTransactionState(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
