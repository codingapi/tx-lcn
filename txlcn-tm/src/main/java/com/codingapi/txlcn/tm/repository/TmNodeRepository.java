package com.codingapi.txlcn.tm.repository;

import java.util.List;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-9-13 23:12:57
 */
public interface TmNodeRepository {

    List<String> keys(String pattern);

    TmNodeInfo getTmNodeInfo(String key);

    void create(String tmId, String hostAndPort, int connection);
}
