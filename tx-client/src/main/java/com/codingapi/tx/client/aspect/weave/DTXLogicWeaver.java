package com.codingapi.tx.client.aspect.weave;

import com.codingapi.tx.client.aspect.BusinessCallback;
import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.spi.sleuth.TracerHelper;
import com.codingapi.tx.client.support.separate.TXLCNTransactionServiceExecutor;
import com.codingapi.tx.commons.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author ujued
 */
@Component
@Slf4j
public class DTXLogicWeaver {

    private final TracerHelper tracerHelper;

    private final TXLCNTransactionServiceExecutor transactionServiceExecutor;

    @Autowired
    public DTXLogicWeaver(TracerHelper tracerHelper,
                          TXLCNTransactionServiceExecutor transactionServiceExecutor) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
    }

    public Object runTransaction(DTXInfo dtxInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocal.cur())) {
            DTXLocal.getOrNew();
        } else {
            return business.call();
        }

        log.info("tx-unit start---->");
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
        TxTransactionInfo info = new TxTransactionInfo(dtxInfo.getTransactionType(), isTransactionStart,
                groupId, unitId, dtxInfo.getTransactionInfo(), business,
                dtxInfo.getBusinessMethod(), dtxInfo.getTransactionPropagation());

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            synchronized (DTXLocal.cur()) {
                DTXLocal.cur().notifyAll();
            }
            DTXLocal.makeNeverAppeared();
            log.info("tx-unit end------>");
        }
    }
}
