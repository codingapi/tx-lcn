package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import com.codingapi.txlcn.tm.repository.redis.RedisTmNodeRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author lorne
 * @date 2020/9/3
 * @description
 */
@Configuration
public class MockRedisTemplateConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = Mockito.mock(RedisTemplate.class);
        ValueOperations valueOperations = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.when(redisTemplate.hasKey(Mockito.any())).thenReturn(true);
        Mockito.when(valueOperations.setIfAbsent(Mockito.any(), Mockito.any(),Mockito.anyLong(),Mockito.any(TimeUnit.class))).thenReturn(true);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public TmNodeRepository tmNodeRepository(RedisTemplate<String, Object> tmRedisTemplate) {
        return new RedisTmNodeRepository(tmRedisTemplate);
    }

}
