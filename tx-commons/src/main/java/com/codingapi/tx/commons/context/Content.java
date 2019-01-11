package com.codingapi.tx.commons.context;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
public interface Content {
    <T> T value(String key);
}
