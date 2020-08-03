package com.codingapi.txlcn.tm.repository.redis;

import com.codingapi.txlcn.tm.repository.TransactionGroup;
import com.codingapi.txlcn.tm.repository.TransactionGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author lorne
 * @date 2020/8/3
 * @description
 */
@AllArgsConstructor
public class RedisTransactionGroupRepository implements TransactionGroupRepository {

    private RedisTemplate<String, TransactionGroup> redisTemplate;

    @Override
    public void create(String groupId, String uniqueKey,String moduleName) throws Exception {
        ValueOperations<String,TransactionGroup> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup = new TransactionGroup(groupId,uniqueKey,moduleName);
        operations.set(groupId,transactionGroup);


    }

    @Override
    public void join(String groupId, String uniqueKey, String moduleName) throws Exception {
        ValueOperations<String,TransactionGroup> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup =  operations.get(groupId);
        if(transactionGroup!=null){
            transactionGroup.add(uniqueKey,moduleName);
            operations.set(groupId,transactionGroup);
        }

    }


    @Override
    public void notify(String groupId, boolean success) throws Exception {
        ValueOperations<String,TransactionGroup> operations =  redisTemplate.opsForValue();
        TransactionGroup transactionGroup =  operations.get(groupId);
        if(transactionGroup!=null) {
            transactionGroup.setState(TransactionGroup.TransactionState.parser(success));
            operations.set(groupId,transactionGroup);
        }
    }
}
