package com.codingapi.txlcn.manager.core.context;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
public class SimpleDTXTransaction implements DTXTransaction {

    private String groupId;

    public SimpleDTXTransaction(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String groupId() {
        return groupId;
    }
}
