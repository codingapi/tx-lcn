package com.codingapi.txlcn.tm.config;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.id.IdGenConfiguration;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-31 上午10:53
 *
 * @author ujued
 */
@Component
public class TMIdGenInitializer implements TxLcnInitializer {

    private final FastStorage fastStorage;

    private final TxManagerConfig managerConfig;

    private final IdGenConfiguration idGenConfiguration;

    @Autowired
    public TMIdGenInitializer(FastStorage fastStorage, TxManagerConfig managerConfig, IdGenConfiguration idGenConfiguration) {
        this.fastStorage = fastStorage;
        this.managerConfig = managerConfig;
        this.idGenConfiguration = idGenConfiguration;
    }

    @Override
    public void init() throws Exception {
        int value = fastStorage.acquireMachineId(managerConfig.getHost() + ":" + managerConfig.getPort(), managerConfig.getMachineIdLen());
        idGenConfiguration.setMachineId(value);
    }

    @Override
    public void destroy() throws Exception {
        fastStorage.releaseMachineId(managerConfig.getHost() + ":" + managerConfig.getPort());
    }
}
