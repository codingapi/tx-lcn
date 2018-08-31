package com.codingapi.lcn.tx.annotation;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
public enum TxTransactionMode {

    /** LCN 模式 */
    TX_MODE_LCN("LCN 模式,2阶段提交 读提交"),

    /** TXC 模式 */
    TX_MODE_TXC("TXC 模式,未提交读(READ UNCOMMITTED)"),

    /** TCC 模式 */
    TX_MODE_TCC("TCC 模式,未提交读(READ UNCOMMITTED)");

    private String description;

    TxTransactionMode(String description) {
        this.description = description;
    }
}
