package com.codingapi.txlcn.common.util.id;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Date: 19-1-31 上午11:32
 *
 * @author ujued
 */
@Slf4j
public abstract class IdGenInit {

    public static void applySnowFlakeIdGen(int machineLen, int machineId) {
        SnowFlakeGenerator.Factory factory = new SnowFlakeGenerator.Factory(machineLen, 0);
        SnowFlakeGenerator generator = factory.create(0, machineId);
        RandomUtils.init(() -> {
            log.debug("Used SnowFlakeIdGen.");
            return generator.nextId();
        });
    }
}
