package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.common.util.id.IdGenInit;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.listener.HeartbeatListener;
import com.codingapi.txlcn.txmsg.listener.RpcConnectionListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final TxLogger txLogger = TxLogger.newLogger(EnsureIdGenEngine.class);

    private final ModIdProvider modIdProvider;

    @Autowired
    public EnsureIdGenEngine(ManagerService managerService, TxManagerConfig managerConfig, ModIdProvider modIdProvider) {
        this.managerService = managerService;
        this.managerConfig = managerConfig;
        this.modIdProvider = modIdProvider;
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
        managerConfig.applyMachineId(managerService.machineIdSync());
        IdGenInit.applyDefaultIdGen(managerConfig.getSeqLen(), managerConfig.getMachineId());

        Transactions.setApplicationIdWhenRunning(modIdProvider.modId());
    }

    @Override
    public void onTmReceivedHeart(RpcCmd cmd) {
        try {
            Long machineId = cmd.getMsg().loadBean(Long.class);
            managerService.refreshMachines(machineId, managerConfig.getMachineId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
