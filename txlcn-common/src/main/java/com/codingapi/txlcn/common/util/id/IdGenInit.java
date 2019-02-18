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

    /**
     * 原版雪花算法，生成一个long类型的ID
     *
     * @param machineLen 机器位长度
     * @param machineId  机器ID
     */
    public static void applySnowFlakeIdGen(int machineLen, long machineId) {
        SnowFlakeGenerator.Factory factory = new SnowFlakeGenerator.Factory(machineLen, 0);
        SnowFlakeGenerator snowFlakeGenerator = factory.create(0, machineId);
        RandomUtils.init(() -> String.valueOf(snowFlakeGenerator.nextId()));
    }

    /**
     * 默认ID生成。和雪花组成一样，加长了机器位，生成了更长的ID
     *
     * @param seqLen    序列位长度
     * @param machineId 机器ID
     */
    public static void applyDefaultIdGen(int seqLen, long machineId) {
        RandomUtils.init(new DefaultIdGen(seqLen, machineId));
    }
}
