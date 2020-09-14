package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.google.common.collect.Maps;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-02 22:51:37
 */

public class AnalyseStrategryFactory {

    private static Map<String, SqlSqlAnalyseHandler> strategyMap = Maps.newHashMap();

    public static SqlSqlAnalyseHandler getInvokeStrategy(String name) {
        return strategyMap.get(name);
    }

    public static void register(String name, SqlSqlAnalyseHandler handler) {
        if (StringUtils.isEmpty(name) || null == handler) {
            return;
        }
        strategyMap.put(name, handler);
    }

}
