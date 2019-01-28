package com.codingapi.txlcn.tracing;

import com.codingapi.txlcn.commons.util.Maps;
import com.codingapi.txlcn.commons.util.RandomUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-28 下午4:21
 *
 * @author ujued
 */
public class TracingContext {

    private static ThreadLocal<TracingContext> tracingContextThreadLocal = new ThreadLocal<>();

    private TracingContext() {

    }

    public static TracingContext tracingContext() {
        if (tracingContextThreadLocal.get() == null) {
            tracingContextThreadLocal.set(new TracingContext());
        }
        return tracingContextThreadLocal.get();
    }

    private Map<String, String> fields = new HashMap<>();

    public String beginTransactionGroup() {
        if (hasGroup()) {
            return groupId();
        }
        init(Maps.newHashMap(TracingConstants.GROUP_ID, RandomUtils.randomKey(), TracingConstants.APP_LIST, ""));
        return fields.get(TracingConstants.GROUP_ID);
    }

    public void init(Map<String, String> initFields) {
        this.fields.putAll(initFields);
    }

    public boolean hasGroup() {
        return fields.containsKey(TracingConstants.GROUP_ID) &&
                StringUtils.hasText(fields.get(TracingConstants.GROUP_ID));
    }

    public String groupId() {
        if (hasGroup()) {
            return fields.get(TracingConstants.GROUP_ID);
        }
        throw new IllegalStateException("non group id.");
    }

    public void addApp(String appId) {
        if (hasGroup()) {
            this.fields.put(TracingConstants.APP_LIST,
                    String.join(",", this.fields.get(TracingConstants.APP_LIST), appId));
        }
        throw new IllegalStateException("non group id.");
    }

    public String appList() {
        if (hasGroup()) {
            return this.fields.get(TracingConstants.APP_LIST);
        }
        throw new IllegalStateException("non group id.");
    }
}
