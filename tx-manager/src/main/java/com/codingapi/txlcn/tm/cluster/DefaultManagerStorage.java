package com.codingapi.txlcn.tm.cluster;

import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.commons.exception.FastStorageException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 19-1-21 下午5:39
 *
 * @author ujued
 */
@Component
public class DefaultManagerStorage implements ManagerStorage, DisposableBean {

    private final FastStorage fastStorage;

    private final TxManagerConfig txManagerConfig;

    @Autowired
    public DefaultManagerStorage(FastStorage fastStorage, TxManagerConfig txManagerConfig) {
        this.fastStorage = fastStorage;
        this.txManagerConfig = txManagerConfig;
    }

    @Override
    public List<String> addressList() {
        try {
            return fastStorage.findTMAddresses();
        } catch (FastStorageException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void remove(String address) {
        try {
            fastStorage.removeTMAddress(address);
        } catch (FastStorageException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void destroy() throws Exception {
        fastStorage.removeTMAddress(txManagerConfig.getHost() + ":" + txManagerConfig.getPort());
    }
}
