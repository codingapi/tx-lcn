package com.codingapi.tx.annotation;

/**
 *
 *
 * @author caican
 *         2018/7/24
 */
public enum TxTransactionMode {
    /** LCN 模式 */
    TX_MODE_LCN("LCN 模式,2阶段提交 读提交"),

    /** TXC 模式 */
    TX_MODE_TXC("TXC 模式,未提交读(READ UNCOMMITTED)");


    private String description;

    TxTransactionMode(String description) {
        this.description = description;
    }
}
