package com.codingapi.txlcn.client.support.common;

import java.util.ArrayList;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
public class TransactionUnitTypeList extends ArrayList<String> {

    /**
     * 添加一个元素并返回列表自身
     *
     * @param transactionType
     * @return
     */
    public TransactionUnitTypeList selfAdd(String transactionType) {
        this.add(transactionType);
        return this;
    }
}
