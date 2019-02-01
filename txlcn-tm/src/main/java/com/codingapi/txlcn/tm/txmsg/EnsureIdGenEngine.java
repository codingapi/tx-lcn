package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.common.util.id.IdGenInit;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.listener.HeartbeatListener;
import com.codingapi.txlcn.txmsg.listener.RpcConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-31 上午11:18
 *
 * @author ujued
 */
@Component
public class EnsureIdGenEngine implements RpcConnectionListener, HeartbeatListener, TxLcnInitializer {

    private final TxManagerConfig managerConfig;

    private final ManagerService managerService;

    private final TxLogger txLogger;

    private final ConfigurableEnvironment environment;

    private final ServerProperties serverProperties;

    @Autowired
    public EnsureIdGenEngine(ManagerService managerService, TxManagerConfig managerConfig, TxLogger txLogger,
                             ConfigurableEnvironment environment, ServerProperties serverProperties) {
        this.managerService = managerService;
        this.managerConfig = managerConfig;
        this.txLogger = txLogger;
        this.environment = environment;
        this.serverProperties = serverProperties;
    }

    @Override
    public void connect(String remoteKey) {
        // nothing to do.
    }

    @Override
    public void disconnect(String remoteKey, String appName) {
    }

    @Override
    public void init() throws Exception {
        IdGenInit.applyDefaultIdGen(managerConfig.getSeqLen(), managerService.machineIdSync());

        Transactions.setApplicationIdWhenRunning(ApplicationInformation.modId(environment, serverProperties));
    }

    @Override
    public void onTmReceivedHeart(RpcCmd cmd) {
        try {
            int machineId = cmd.getMsg().loadBean(Integer.class);
            managerService.refreshMachineId(machineId);
        } catch (Exception e) {
            txLogger.error("onTmReceivedHeart", e.getMessage());
        }
    }
}
