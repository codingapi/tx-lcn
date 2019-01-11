package com.codingapi.tx.manager.core;

/**
 * Description:
 * Date: 19-1-11 下午6:07
 *
 * @author ujued
 */
public class GroupTransaction {

    private String groupId;

    public GroupTransaction(String groupId) {
        this.groupId = groupId;
    }

    public String groupId() {
        return groupId;
    }
}
