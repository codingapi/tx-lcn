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
        RandomUtils.init(new DefaultIdGen(machineLen, 12, machineId));
    }
}
