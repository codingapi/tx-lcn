package com.codingapi.tx.client.spi.transaction.txc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.TxcService;
import com.codingapi.tx.client.spi.transaction.txc.resource.def.bean.RollbackInfo;
import com.codingapi.tx.client.support.common.TransactionCleanService;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxcLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcTransactionCleanService implements TransactionCleanService {

    private final TxcService txcService;

    @Autowired
    public TxcTransactionCleanService(TxcService txcService) {
        this.txcService = txcService;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        try {
            try {
                // 若需要回滚读undo_log，进行回滚
                if (state != 1 && state != -1) {
                    txcService.undo(groupId, unitId);
                }
            } catch (Exception e) {
                if (Objects.nonNull(DTXLocal.cur())) {
                    RollbackInfo rollbackInfo = (RollbackInfo) DTXLocal.cur().getAttachment();
                    if (Objects.nonNull(rollbackInfo)) {
                        txcService.undoByRollbackInfo(rollbackInfo);
                    }
                }
            }

            // 清理TXC
            txcService.cleanTxc(groupId, unitId);
        } catch (TxcLogicException e) {
            log.error("txc > clean transaction error. {}", e.getMessage());
            throw new TransactionClearException(e.getMessage());
        }
    }
}
