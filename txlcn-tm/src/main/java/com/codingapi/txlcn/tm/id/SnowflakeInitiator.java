package com.codingapi.txlcn.tm.id;


import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

import static com.codingapi.txlcn.tm.constant.CommonConstant.TX_MANAGER;

/**
 * 雪花算法初始器
 * 初始化snowflake的 dataCenterId 和 workerId
 * <p>
 * 1.系统启动时生成默认 dataCenterId 和 workerId，并尝试作为 key 存储到 redis
 * 2.如果存储成功，设置 redis 过期时间为24h，把当前 dataCenterId 和 workerId 传入 snowflake
 * 3.如果存储失败 workerId 自加1，并判断 workerId 不大于31，重复1步骤
 * 4.定义一个定时器，每隔 24h 刷新 redis 的过期时间为 24h
 *
 * @author whohim
 */
@SuppressWarnings({"ConstantConditions"})
@Configuration
@Slf4j
public class SnowflakeInitiator {

    @Value("${txlcn.protocol.port}")
    private String port;

    private static String snowflakeRedisKey;

    private static long LockExpire = 30;

    private static boolean stopTrying = false;

    /**
     * snowflake 的 dataCenterId 和 workerId
     */
    public static SnowflakeVo snowflakeVo;

    private final StringRedisTemplate redisTemplate;

    public SnowflakeInitiator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public void init() {
        if (tryInit()) {
            log.debug("snowflake try Init create key,key:{}", JSON.toJSONString(snowflakeVo));
            return;
        }
        if (stopTrying) {
            log.debug("snowflake stop create key,key:{}", JSON.toJSONString(snowflakeVo));
            return;
        }
        init();
    }

    /**
     * 尝试保存 workId 和 dataCenterId 到 redis
     *
     * @return boolean
     */
    public boolean tryInit() {
        snowflakeVo = nextKey(snowflakeVo);
        snowflakeRedisKey = String.format("%s_%d_%d", TX_MANAGER, snowflakeVo.getDataCenterId(),
                snowflakeVo.getWorkerId());
        String hostAddress = NetUtil.getLocalhost().getHostAddress();
        String hostAndPort = String.format("%s:%s", hostAddress, port);
        Boolean isNotHasKey = !redisTemplate.hasKey(snowflakeRedisKey);
        Boolean isSetKey = redisTemplate.opsForValue().setIfAbsent(snowflakeRedisKey, hostAndPort,
                LockExpire + randomDigits(), TimeUnit.SECONDS);
        if (isNotHasKey && isSetKey) {
            log.info("snowflake setIfAbsent key:{}", JSON.toJSONString(snowflakeVo));
            return true;
        }

        return false;
    }

    /**
     * 生成下一组不重复的 dataCenterId 和 workerId
     *
     * @return SnowflakeVo
     */
    private SnowflakeVo nextKey(SnowflakeVo snowflakeVo) {
        if (snowflakeVo == null) {
            return new SnowflakeVo(1L, 1L);
        }

        if (snowflakeVo.getWorkerId() < 31) {
            // 如果workerId < 31
            snowflakeVo.setWorkerId(snowflakeVo.getWorkerId() + 1);
        } else {
            // 如果workerId >= 31
            if (snowflakeVo.getDataCenterId() < 31) {
                // 如果workerId >= 31 && dataCenterId < 31
                snowflakeVo.setDataCenterId(snowflakeVo.getDataCenterId() + 1);
                snowflakeVo.setWorkerId(1L);
            } else {
                // 如果workerId >= 31 && dataCenterId >= 31
                snowflakeVo.setDataCenterId(1L);
                snowflakeVo.setWorkerId(1L);
                stopTrying = true;
            }
        }
        return snowflakeVo;
    }

    private int randomDigits() {
        return (int) (Math.random() * 10);
    }

    /**
     * 重新设置过期时间，由定时任务调用
     */
    public void resetExpire() {
        redisTemplate.expire(snowflakeRedisKey, (LockExpire + randomDigits()), TimeUnit.SECONDS);
        log.info("reset the snowflakeRedisKey's resetExpire time,redisKey :{}", snowflakeRedisKey);
    }

    /**
     * 容器销毁时主动删除 redis 注册记录，此方法不适用于强制终止 Spring 容器的场景，只作为补充优化
     */
    @PreDestroy
    public void destroy() {
        redisTemplate.delete(snowflakeRedisKey);
    }

    public static SnowflakeVo getSnowflakeVo() {
        return snowflakeVo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SnowflakeVo {
        private Long dataCenterId;
        private Long workerId;
    }
}
