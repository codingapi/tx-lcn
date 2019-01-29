package com.codingapi.txlcn.tracing;

import com.codingapi.txlcn.commons.util.Maps;
import com.codingapi.txlcn.commons.util.RandomUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    private Map<String, String> fields;

    public void beginTransactionGroup() {
        if (hasGroup()) {
            return;
        }
        init(Maps.newHashMap(TracingConstants.GROUP_ID, RandomUtils.randomKey(), TracingConstants.APP_LIST, ""));
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
        throw new IllegalStateException("non group id.");
    }

    public void addApp(String appId) {
        if (hasGroup()) {
            this.fields.put(TracingConstants.APP_LIST,
                    String.join(",", this.fields.get(TracingConstants.APP_LIST), appId));
        }
        throw new IllegalStateException("non group id.");
    }

    public String appListString() {
        if (hasGroup()) {
            return this.fields.get(TracingConstants.APP_LIST);
        }
        throw new IllegalStateException("non group id.");
    }

    public List<String> appList() {
        if (hasGroup()) {
            return Arrays.stream(this.fields.get(TracingConstants.APP_LIST).split(",")).collect(Collectors.toList());
        }
        throw new IllegalStateException("non group id.");
    }

    public void destroy() {
        this.fields = null;
    }
}
