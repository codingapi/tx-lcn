package com.codingapi.tx.client.spi.transaction.txc.resource.init;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.config.TxcConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public class DefaultTxcSettingFactory implements TxcSettingFactory {

    @Autowired
    private TxcConfig txcConfig;

    @Override
    public String lockTableSql() {
        return txcConfig.getLockTableSql();
    }

    @Override
    public String undoLogTableSql() {
        return txcConfig.getUndoLogTableSql();
    }

    @Override
    public boolean enable() {
        return txcConfig.isEnable();
    }

    @Override
    public String lockTableName() {
        return txcConfig.getLockTableName();
    }

    @Override
    public String undoLogTableName() {
        return txcConfig.getUndoLogTableName();
    }
}
