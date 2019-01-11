package com.codingapi.tx.commons.context;

import java.util.Map;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
public interface GenericContext extends Context {

    void hold(String id, String key, Object value);

    Content content(String id);

    <T> T valueOfContent(String id, String key);

    void discard(String id);

    void discard(String id, String key);
}
