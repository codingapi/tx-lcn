package com.codingapi.txlcn.tm.config;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.id.RandomUtils;
import com.codingapi.txlcn.common.util.id.SnowFlakeGenerator;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-31 上午10:53
 *
 * @author ujued
 */
@Component
@Slf4j
public class TMIdGenInitializer implements TxLcnInitializer {

    private final FastStorage fastStorage;

    private final TxManagerConfig managerConfig;

    @Autowired
    public TMIdGenInitializer(FastStorage fastStorage, TxManagerConfig managerConfig) {
        this.fastStorage = fastStorage;
        this.managerConfig = managerConfig;
    }

    @Override
    public void init() throws Exception {
        int value = fastStorage.acquireMachineId(managerConfig.getHost() + ":" + managerConfig.getPort(), managerConfig.getMachineIdLen());
        SnowFlakeGenerator.Factory factory = new SnowFlakeGenerator.Factory(managerConfig.getMachineIdLen(), 0);
        SnowFlakeGenerator generator = factory.create(0, value);
        RandomUtils.init(generator::nextId);
        log.info("MachineId is {}.", value);
    }

    @Override
    public void destroy() throws Exception {
        fastStorage.releaseMachineId(managerConfig.getHost() + ":" + managerConfig.getPort());
    }
}
