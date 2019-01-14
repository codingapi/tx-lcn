package com.codingapi.txlcn.client.core.lcn.control;

import com.codingapi.txlcn.client.core.lcn.resource.LCNConnectionProxy;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.txlcn.client.support.common.TransactionCleanService;
import com.codingapi.txlcn.spi.message.dto.RpcResponseState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class LcnTransactionCleanService implements TransactionCleanService {

    private final TransactionAttachmentCache transactionAttachmentCache;

    @Autowired
    public LcnTransactionCleanService(TransactionAttachmentCache transactionAttachmentCache) {
        this.transactionAttachmentCache = transactionAttachmentCache;
    }


    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        Optional<LCNConnectionProxy> lcnConnectionProxy = transactionAttachmentCache.attachment(groupId, LCNConnectionProxy.class);
        if (lcnConnectionProxy.isPresent()) {
            if (lcnConnectionProxy.get().notify(state).equals(RpcResponseState.success)) {
                // 移除本地LCN事务相关对象
                transactionAttachmentCache.removeAttachments(groupId, unitId);
                return;
            }
            log.error("本地事务通知失败");
            throw new TransactionClearException("通知资源时出错");
        }
        log.error("local non transaction, but notified. probably net message timeout . groupId: {}, state: {}", groupId, state);
        throw new TransactionClearException("local non transaction, but notified. probably net message timeout .");
    }
}
