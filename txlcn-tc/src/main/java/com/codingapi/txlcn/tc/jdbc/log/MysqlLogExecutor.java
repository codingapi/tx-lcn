package com.codingapi.txlcn.tc.jdbc.log;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class MysqlLogExecutor implements LogExecutor {

    @Override
    public String insert(TransactionLog transactionLog) {
        return "insert into `transaction_log`(`id`,`group_id`,`sql`,`time`,`flag`) values(?,?,?,?,?)";
    }

    @Override
    public String create() {
        return null;
    }

    @Override
    public String delete(long id) {
        return null;
    }
}
