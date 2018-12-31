package com.codingapi.tx.client.spi.transaction.txc.resource.sql.init;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public class DefaultTxcSettingFactory implements TxcSettingFactory {

    @Override
    public String lockTableSql() {
        return "CREATE TABLE `" + lockTableName() + "`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `table_name` varchar(64) NULL DEFAULT NULL,\n" +
                "  `key_value` varchar(32) NULL DEFAULT NULL,\n" +
                "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `x_lock` int(11) NULL DEFAULT NULL,\n" +
                "  `s_lock` int(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE INDEX `table_name`(`table_name`, `key_value`, `x_lock`) USING BTREE\n" +
                ")";
    }

    @Override
    public String undoLogTableSql() {
        return "CREATE TABLE `" + undoLogTableName() + "`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `gmt_create` bigint(20) NULL DEFAULT NULL,\n" +
                "  `gmt_modified` bigint(20) NULL DEFAULT NULL,\n" +
                "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `rollback_info` blob NULL DEFAULT NULL,\n" +
                "  `status` int(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ")";
    }

}
