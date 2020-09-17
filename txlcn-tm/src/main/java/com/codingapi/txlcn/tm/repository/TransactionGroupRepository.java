package com.codingapi.txlcn.tm.repository;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
public interface TransactionGroupRepository {

    void create(String groupId, String uniqueKey,String moduleName) throws Exception;

    void join(String groupId, String uniqueKey,String moduleName) throws Exception;

    TransactionGroup notify(String groupId, boolean success) throws Exception;

}
