package com.codingapi.tx.manager.support.group;

import com.codingapi.tx.commons.exception.JoinGroupException;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/3
 *
 * @author ujued
 */

public interface GroupRelationship {

    void createGroup(String groupId);

    void joinGroup(String groupId, TransUnit transUnit) throws JoinGroupException;

    List<TransUnit> unitsOfGroup(String groupId);

    void removeGroup(String groupId);

    void setTransactionState(String groupId, Short state);

    Short transactionState(String groupId);
}
