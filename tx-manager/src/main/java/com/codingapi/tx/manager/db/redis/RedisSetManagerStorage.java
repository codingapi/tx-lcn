package com.codingapi.tx.manager.db.redis;

import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.db.ManagerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manager信息改用Set 暂未使用
 *
 * @author meetzy
 * @date 2019-01-14 11:09
 */
@Slf4j
public class RedisSetManagerStorage implements ManagerStorage {
    
    private static final String REDIS_PREFIX = "tx.manager.list";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private TxManagerConfig managerConfig;
    
    @Value("${server.port}")
    private int port;
    
    @Override
    public List<String> addressList() {
        return new ArrayList<>(Objects.requireNonNull(redisTemplate.opsForSet().members(REDIS_PREFIX)));
    }
    
    @Override
    public void remove(String address) {
        redisTemplate.opsForSet().remove(REDIS_PREFIX, address);
    }
    
    @PostConstruct
    public void init() {
        String address = managerConfig.getHost() + ":" + port;
        redisTemplate.opsForSet().add(REDIS_PREFIX, address);
        log.info("manager add redis finish.");
    }
    
    @PreDestroy
    public void destroy() {
        String address = managerConfig.getHost() + ":" + port;
        remove(address);
        log.info("manager remove redis.");
    }
}
