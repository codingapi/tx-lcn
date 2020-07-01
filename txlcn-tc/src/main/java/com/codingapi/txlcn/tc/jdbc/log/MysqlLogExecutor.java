package com.codingapi.txlcn.tc.jdbc.log;

import com.google.common.base.Joiner;

import java.util.List;

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
    public String delete(List<Long> ids) {
        String id = Joiner.on(",").join(ids);
        return "delete from `transaction_log` where id in ("+id+")";
    }
}
