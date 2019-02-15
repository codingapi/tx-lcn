/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tracing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.common.util.id.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public static void init(Map<String, String> initFields) {
        TracingContext tracingContext = tracing();
        if (Objects.isNull(tracingContext.fields)) {
            tracingContext.fields = new HashMap<>();
        }
        //APP_MAP base64 解码
        if (initFields.containsKey(TracingConstants.APP_MAP)) {
            String appMapVal = initFields.get(TracingConstants.APP_MAP);
            if (!appMapVal.startsWith("{") || !appMapVal.contains("{")) {
                initFields.put(TracingConstants.APP_MAP, tracingContext.baseString2appMap(appMapVal));
            }
        }
        tracingContext.fields.putAll(initFields);
        tracingContext.fields.put(TracingConstants.THREAD_ID, String.valueOf(Thread.currentThread().getId()));
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

    public Map<String, String> fields() {
        return this.fields;
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

    public String appMapBase64String() {
        if (hasGroup()) {
            return Base64Utils.encodeToString(this.fields.get(TracingConstants.APP_MAP).getBytes(Charset.forName("utf8")));
        }
        raiseNonGroupException();
        return "";
    }

    private String baseString2appMap(String base64Str) {
        //解码
        if (!"".equals(base64Str)) {
            base64Str = Base64Utils.encodeToString(base64Str.getBytes(Charset.forName("utf8")));
        }
        return base64Str;
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
        if (Objects.nonNull(tracingContextThreadLocal.get())) {
            tracingContextThreadLocal.set(null);
        }
    }

    private void raiseNonGroupException() {
        throw new IllegalStateException("non group id.");
    }
}
