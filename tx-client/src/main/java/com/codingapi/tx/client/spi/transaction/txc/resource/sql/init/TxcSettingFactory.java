package com.codingapi.tx.client.spi.transaction.txc.resource.sql.init;

import com.codingapi.tx.client.spi.transaction.txc.resource.sql.util.SqlUtils;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public interface TxcSettingFactory {

    /**
     * 允许TXC事务模式
     *
     * @return
     */
    default boolean enable() {
        return true;
    }

    /**
     * 锁表名称
     *
     * @return
     */
    default String lockTableName() {
        return SqlUtils.LOCK_TABLE;
    }

    /**
     * 撤销SQL信息表名
     *
     * @return
     */
    default String undoLogTableName() {
        return SqlUtils.UNDO_LOG_TABLE;
    }

    /**
     * 事务锁表创建SQL
     *
     * @return
     */
    String lockTableSql();

    /**
     * 撤销日志表创建SQL
     *
     * @return
     */
    String undoLogTableSql();
}
