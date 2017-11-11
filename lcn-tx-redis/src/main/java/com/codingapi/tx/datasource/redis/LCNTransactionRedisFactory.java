package com.codingapi.tx.datasource.redis;


import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.AbstractResourceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;

/**
 * create by lorne on 2017/8/22
 */
public class LCNTransactionRedisFactory extends AbstractResourceProxy<RedisConnection,LCNRedisConnection> implements RedisConnectionFactory {

    private Logger logger = LoggerFactory.getLogger(LCNTransactionRedisFactory.class);

    private RedisConnectionFactory redisConnectionFactory;

    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }


    @Override
    protected RedisConnection createLcnConnection(RedisConnection connection, TxTransactionLocal txTransactionLocal) {
        nowCount++;
        LCNRedisConnection lcn = new LCNRedisConnection(connection, dataSourceService, txTransactionLocal, subNowCount);
        pools.put(txTransactionLocal.getGroupId(), lcn);
        logger.info("get new connection ->" + txTransactionLocal.getGroupId());
        return lcn;
    }


    @Override
    protected void initDbType() {

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();
        if(txTransactionLocal!=null) {
            //设置db类型
            txTransactionLocal.setType("redis");
        }
    }

    @Override
    public RedisConnection getConnection() {

        initDbType();

        RedisConnection redisConnection = loadConnection();
        if(redisConnection==null) {
            redisConnection =  initLCNConnection(redisConnectionFactory.getConnection());
            if(redisConnection==null){
                throw new RuntimeException("connection was overload");
            }
            return redisConnection;
        }else {
            return redisConnection;
        }
    }


    @Override
    public RedisClusterConnection getClusterConnection() {
        System.out.println("getClusterConnection");
        return redisConnectionFactory.getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return redisConnectionFactory.getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        System.out.println("getSentinelConnection");
        return redisConnectionFactory.getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return redisConnectionFactory.translateExceptionIfPossible(ex);
    }
}
