package com.codingapi.tx.commons.context;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
@Data
public class MapBasedContent implements Content {

    private Map<String, Object> cache = new HashMap<>();
    private Set<String> keys = new HashSet<>();


    @Override
    public <T> T value(String key) {
        return (T) this.cache.get(key);
    }
}
