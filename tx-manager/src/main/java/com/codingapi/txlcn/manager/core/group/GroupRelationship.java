package com.codingapi.txlcn.manager.core.group;

import com.codingapi.txlcn.commons.exception.JoinGroupException;

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

    void setTransactionState(String groupId, int state);

    Short transactionState(String groupId);
}
