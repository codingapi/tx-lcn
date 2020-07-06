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
        return "\n" +
                "CREATE TABLE `transaction_log` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ' ',\n" +
                "  `group_id` varchar(40) DEFAULT NULL,\n" +
                "  `sql` varchar(255) DEFAULT NULL,\n" +
                "  `time` bigint(20) DEFAULT NULL,\n" +
                "  `flag` int(1) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
                "\n" ;
    }

    @Override
    public String delete(List<Long> ids) {
        String id = Joiner.on(",").join(ids);
        return "delete from `transaction_log` where id in ("+id+")";
    }
}
