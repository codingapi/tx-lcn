package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.commons.exception.FastStorageException;
import com.codingapi.txlcn.commons.util.ApplicationInformation;
import com.codingapi.txlcn.spi.message.listener.TCRegisterListener;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-24 下午6:07
 *
 * @author ujued
 */
@Component
public class ClusterAndTCRelationRecorder implements TCRegisterListener {

    private final FastStorage fastStorage;

    private final String tmId;

    @Autowired
    public ClusterAndTCRelationRecorder(FastStorage fastStorage, ConfigurableEnvironment environment, ServerProperties serverProperties) {
        this.fastStorage = fastStorage;
        this.tmId = ApplicationInformation.modId(environment, serverProperties);
    }

    @Override
    public void onRegister(String modId) {
        try {
            fastStorage.saveModIdAndTM(modId, tmId);
        } catch (FastStorageException e) {
            // TODO
        }
    }

    @Override
    public void onCancel(String modId) {
        try {
            fastStorage.deleteModIdAndTM(modId, modId);
        } catch (FastStorageException e) {
            // TODO
        }
    }
}
