package com.codingapi.tx.commons.context;

/**
 * Description:
 * Date: 1/11/19
 *
 * @author ujued
 */
public interface Context {

    <T> T attribute(String key);
}
