package com.codingapi.txlcn.spi.message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 19-1-24 下午4:26
 *
 * @author ujued
 */
public class TMCluster {
    private Map<String, String> relation = new ConcurrentHashMap<>();

    public List<String> tmKeys() {
        return new ArrayList<>(relation.values());
    }

    public void toCluster(String tmId, String rpcKey) {
        relation.put(tmId, rpcKey);
    }

    public Map<String, String> relation() {
        return relation;
    }

    public void cleanCluster(String remoteKey) {
        relation.entrySet().removeIf(entry -> entry.getValue().equals(remoteKey));
    }

    public String rpcKey(String tmId) {
        return relation.get(tmId);
    }
}
