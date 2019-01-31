package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.id.IdGenInit;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import com.codingapi.txlcn.txmsg.listener.RpcConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Description:
 * Date: 19-1-31 上午11:18
 *
 * @author ujued
 */
@Component
public class EnsureIdGenEngine implements RpcConnectionListener, TxLcnInitializer {

    private final TxManagerConfig managerConfig;

    private final ManagerService managerService;

    @Autowired
    public EnsureIdGenEngine(ManagerService managerService, TxManagerConfig managerConfig) {
        this.managerService = managerService;
        this.managerConfig = managerConfig;
    }

    @Override
    public void connect(String remoteKey) {
        // nothing to do.
    }

    @Override
    public void disconnect(String remoteKey, String appName) {
        if (StringUtils.hasText(appName)) {
            managerService.releaseMachineId(appName);
        }
    }

    @Override
    public void init() throws Exception {
        int value = managerService.acquireMachineId(managerConfig.getHost(), managerConfig.getPort());
        IdGenInit.applySnowFlakeIdGen(managerConfig.getMachineIdLen(), value);
    }

    @Override
    public void destroy() {
        managerService.releaseMachineId(managerConfig.getHost(), managerConfig.getPort());
    }
}
