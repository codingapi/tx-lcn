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
}
