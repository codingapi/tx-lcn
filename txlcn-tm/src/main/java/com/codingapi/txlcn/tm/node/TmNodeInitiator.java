package com.codingapi.txlcn.tm.node;


import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tm.reporter.TmManagerReporter;
import com.codingapi.txlcn.tm.repository.TmNodeInfo;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

import static com.codingapi.txlcn.tm.constant.CommonConstant.TX_MANAGER;

/**
 * TmNode 初始器
 * 初始化 snowflake 的 dataCenterId 和 workerId
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
public class TmNodeInitiator {

    @Value("${txlcn.protocol.port}")
    private String port;

    private static String tmId;

    private static long LockExpire = 30;

    private static boolean stopTrying = false;

    /**
     * snowflake 的 dataCenterId 和 workerId
     */
    public static SnowflakeVo snowflakeVo;

    final private TmNodeRepository tmNodeRepository;

    public TmNodeInitiator(TmNodeRepository tmNodeRepository) {
        this.tmNodeRepository = tmNodeRepository;
    }

    @Bean
    public void init() {
        if (tryInit()) {
            log.debug("TmNode try Init create key,key:{}", JSON.toJSONString(snowflakeVo));
            return;
        }
        if (stopTrying) {
            log.debug("TmNode stop create key,key:{}", JSON.toJSONString(snowflakeVo));
            return;
        }
        init();
    }

    /**
     * 尝试保存初始化
     *
     * @return boolean
     */
    public boolean tryInit() {
        snowflakeVo = this.nextKey(snowflakeVo);
        tmId = String.format("%s_%d_%d", TX_MANAGER, snowflakeVo.getDataCenterId(),
                snowflakeVo.getWorkerId());

        String hostAndPort = String.format("%s:%s", NetUtil.getLocalhost().getHostAddress(), port);
        TmNodeInfo tmNodeInfo = new TmNodeInfo()
                .setTmId(tmId)
                .setHostAndPort(hostAndPort)
                .setConnection(0);

        Boolean isSetKey = tmNodeRepository.setIfAbsent(tmNodeInfo, LockExpire + randomDigits());
        Boolean isHasKey = tmNodeRepository.hasKey(tmId);
        if (isHasKey && isSetKey) {
            log.info("TmNode setIfAbsent key:{}", JSON.toJSONString(snowflakeVo));
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

    /**
     * 随机生成个位数
     */
    private int randomDigits() {
        return (int) (Math.random() * 10);
    }

    /**
     * 重新设置过期时间，由定时任务调用
     *
     * @param tmManagerReporter 拿连接数
     */
    public void resetExpire(TmManagerReporter tmManagerReporter) {
        TmNodeInfo tmNodeInfo = tmNodeRepository.getTmNodeInfo(tmId);
        tmNodeInfo.setConnection(tmManagerReporter.getConnections().size());
        tmNodeRepository.set(tmNodeInfo, (LockExpire + randomDigits()));
        log.info("reset the TmNode's resetExpire time,redisKey :{}", tmId);
    }

    /**
     * 容器销毁时主动删除 redis 注册记录，此方法不适用于强制终止 Spring 容器的场景，只作为补充优化
     */
    @PreDestroy
    public void destroy() {
        tmNodeRepository.delete(tmId);
    }

    public static SnowflakeVo getSnowflakeVo() {
        return snowflakeVo;
    }

    @Data
    @AllArgsConstructor
    public static class SnowflakeVo {
        private Long dataCenterId;
        private Long workerId;
    }
}
