package com.codingapi.tx.manager.core.group;

/**
 * Description:
 * Date: 19-1-9 下午6:09
 *
 * @author ujued
 */
public class TransactionUnit {

    private String unitId;

    private String unitType;

    private String messageContextId;

    public TransactionUnit(String unitId, String unitType, String messageContextId) {
        this.unitId = unitId;
        this.unitType = unitType;
        this.messageContextId = messageContextId;
    }

    public String unitId() {
        return unitId;
    }

    public String messageContextId() {
        return messageContextId;
    }

    public String unitType() {
        return unitType;
    }
}
