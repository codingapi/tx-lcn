package com.codingapi.tx.client.spi.transaction.txc.resource.sql.init;

import com.codingapi.tx.client.spi.transaction.txc.resource.sql.TableStructAnalyser;
import com.codingapi.tx.client.spi.transaction.txc.resource.sql.def.TxcSqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcInitialization implements InitializingBean {

    private final TxcSettingFactory txcSettingFactory;

    private final TableStructAnalyser tableStructAnalyser;

    private final TxcSqlExecutor txcSqlExecutor;

    @Autowired
    public TxcInitialization(TxcSettingFactory txcSettingFactory,
                             TableStructAnalyser tableStructAnalyser,
                             TxcSqlExecutor txcSqlExecutor) {
        this.txcSettingFactory = txcSettingFactory;
        this.tableStructAnalyser = tableStructAnalyser;
        this.txcSqlExecutor = txcSqlExecutor;
    }

    @Override
    public void afterPropertiesSet() {
        if (txcSettingFactory.enable()) {
            log.info("enabled txc transaction.");
            if (!tableStructAnalyser.existsTable(txcSettingFactory.lockTableName())) {
                log.info("create lock table.");
                txcSqlExecutor.createLockTable();
            }
            if (!tableStructAnalyser.existsTable(txcSettingFactory.undoLogTableName())) {
                log.info("create undo log table.");
                txcSqlExecutor.createUndoLogTable();
            }
        }
    }
}
