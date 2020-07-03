package com.codingapi.txlcn.tc.jdbc.log;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public interface LogExecutor {

    String insert(TransactionLog transactionLog);

    String create();

    String delete(List<Long> ids);

    /**
     * 返回数据库找不到表报错时的VendorCode码
     * @return
     */
    int getTableNotFindErrorCode();
}
