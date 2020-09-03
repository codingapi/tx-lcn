package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.protocol.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@Configuration
public class TcConfigConfiguration {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        initRedisTemplate();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initRedisTemplate() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
    }

    @Bean
    @ConfigurationProperties(prefix = "txlcn.tc")
    public TxConfig txConfig(Config config) {
        return new TxConfig(config, redisTemplate);
    }


}
