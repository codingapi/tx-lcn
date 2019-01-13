package com.codingapi.tx.manager.core.context;

import com.codingapi.tx.commons.context.MapBasedContext;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
@Component
public class DTXTransactionContext extends MapBasedContext {

    public DTXTransaction newContext(String groupId) {
        this.hold(groupId, DTXTransaction.class.getName(), new SimpleDTXTransaction(groupId));
        return getTransaction(groupId);
    }

    public DTXTransaction getTransaction(String groupId) {
        return this.valueOfContent(groupId, DTXTransaction.class.getName());
    }

    public void destroyTransaction(String groupId) {
        this.discard(groupId);
    }
}
