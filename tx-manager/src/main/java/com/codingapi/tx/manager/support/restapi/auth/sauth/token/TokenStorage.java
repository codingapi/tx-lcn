package com.codingapi.tx.manager.support.restapi.auth.sauth.token;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @author ujued
 */
public interface TokenStorage {

    boolean exist(String token);

    void add(String token);

    void remove(String token);

    void clear();
}
