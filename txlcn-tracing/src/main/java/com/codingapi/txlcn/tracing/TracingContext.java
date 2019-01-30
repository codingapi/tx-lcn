package com.codingapi.txlcn.tracing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.common.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Description:
 * 1. {@code fields}为 {@code null}。发起方出现，未开始事务组
 * 2. {@code fields}不为空，fields.get(TracingConstants.GROUP_ID) 是 {@code empty}。参与方出现，未开启事务组。
 * 3. TBD
 * Date: 19-1-28 下午4:21
 *
 * @author ujued
 */
@Slf4j
public class TracingContext {

    private static ThreadLocal<TracingContext> tracingContextThreadLocal = new ThreadLocal<>();

    private TracingContext() {

    }

    public static TracingContext tracing() {
        if (tracingContextThreadLocal.get() == null) {
            tracingContextThreadLocal.set(new TracingContext());
        }
        return tracingContextThreadLocal.get();
    }

    private Map<String, String> fields;

    public void beginTransactionGroup() {
        if (hasGroup()) {
            return;
        }
        init(Maps.newHashMap(TracingConstants.GROUP_ID, RandomUtils.randomKey(), TracingConstants.APP_MAP, "{}"));
    }

    public void init(Map<String, String> initFields) {
        if (Objects.isNull(fields)) {
            this.fields = new HashMap<>();
        }
        this.fields.putAll(initFields);
    }

    public boolean hasGroup() {
        return Objects.nonNull(fields) && fields.containsKey(TracingConstants.GROUP_ID) &&
                StringUtils.hasText(fields.get(TracingConstants.GROUP_ID));
    }

    public String groupId() {
        if (hasGroup()) {
            return fields.get(TracingConstants.GROUP_ID);
        }
        raiseNonGroupException();
        return "";
    }

    public void addApp(String serviceId, String address) {
        if (hasGroup()) {
            JSONObject map = JSON.parseObject(this.fields.get(TracingConstants.APP_MAP));
            if (map.containsKey(serviceId)) {
                return;
            }
            map.put(serviceId, address);
            this.fields.put(TracingConstants.APP_MAP, JSON.toJSONString(map));
            return;
        }
        raiseNonGroupException();
    }

    public String appMapString() {
        if (hasGroup()) {
            return this.fields.get(TracingConstants.APP_MAP);
        }
        raiseNonGroupException();
        return "";
    }

    public JSONObject appMap() {
        if (hasGroup()) {
            String appMap = this.fields.get(TracingConstants.APP_MAP);
            log.debug("App map: {}", appMap);
            return JSON.parseObject(appMap);
        }
        raiseNonGroupException();
        return JSON.parseObject("{}");
    }

    public void destroy() {
        this.fields = null;
    }

    private void raiseNonGroupException() {
        throw new IllegalStateException("non group id.");
    }
}
