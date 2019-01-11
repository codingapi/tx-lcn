package com.codingapi.tx.client.aspect.transaction;

import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.separate.TXLCNTransactionServiceExecutor;
import com.codingapi.tx.commons.util.RandomUtils;
import com.codingapi.tx.spi.sleuth.TracerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author ujued
 */
@Component
@Slf4j
public class AspectBeforeServiceExecutor {

    private final TracerHelper tracerHelper;

    private final TXLCNTransactionServiceExecutor transactionServiceExecutor;

    @Autowired
    public AspectBeforeServiceExecutor(TracerHelper tracerHelper,
                                       TXLCNTransactionServiceExecutor transactionServiceExecutor) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
    }

    Object runTransaction(DTXInfo dtxInfo, BusinessSupplier business) throws Throwable {
        log.info("TX-LCN local start---->");
        // 事务发起方判断
        boolean isTransactionStart = tracerHelper.getGroupId() == null;

        // 该线程事务
        String groupId = isTransactionStart ? RandomUtils.randomKey() : tracerHelper.getGroupId();
        String unitId = dtxInfo.getUnitId();
        DTXLocal dtxLocal = DTXLocal.getOrNew();
        if (dtxLocal.getUnitId() != null) {
            dtxLocal.setInUnit(true);
            log.info("tx > unit[{}] in unit: {}", unitId, dtxLocal.getUnitId());
        }
        dtxLocal.setUnitId(unitId);
        dtxLocal.setGroupId(groupId);
        dtxLocal.setTransactionType(dtxInfo.getTransactionType());

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo(
                dtxInfo.getTransactionType(),
                isTransactionStart,
                groupId,
                unitId,
                dtxInfo.getTransactionInfo(),
                business,
                dtxInfo.getBusinessMethod());

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            DTXLocal.makeNeverAppeared();
            log.info("TX-LCN local end------>");
        }
    }
}
