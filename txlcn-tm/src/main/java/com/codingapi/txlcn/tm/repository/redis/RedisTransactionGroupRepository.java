package com.codingapi.txlcn.tm.repository.redis;

import com.codingapi.txlcn.tm.repository.TransactionGroup;
import com.codingapi.txlcn.tm.repository.TransactionGroupRepository;
import com.codingapi.txlcn.tm.repository.TransactionInfo;
import com.codingapi.txlcn.tm.repository.TransactionState;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@AllArgsConstructor
public class RedisTransactionGroupRepository implements TransactionGroupRepository {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void create(String groupId, String uniqueKey,String moduleName) throws Exception {
        ValueOperations<String,Object> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup = new TransactionGroup(groupId,uniqueKey,moduleName, TransactionInfo.TransactionType.REQUEST);
        operations.set(groupId,transactionGroup);


    }

    @Override
    public void join(String groupId, String uniqueKey, String moduleName) throws Exception {
        ValueOperations<String,Object> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup = (TransactionGroup) operations.get(groupId);
        if(transactionGroup!=null){
            transactionGroup.add(uniqueKey,moduleName, TransactionInfo.TransactionType.JOIN);
            operations.set(groupId,transactionGroup);
        }

    }


    @Override
    public TransactionGroup notify(String groupId, boolean success) throws Exception {
        ValueOperations<String,Object> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup = (TransactionGroup) operations.get(groupId);
        if(transactionGroup==null){
            return null;
        }
        transactionGroup.setState(TransactionState.parser(success));
        operations.set(groupId,transactionGroup);
        return transactionGroup;
    }
}
