package com.codingapi.tx.manager.core.restapi.auth.sauth;

import com.codingapi.tx.manager.core.restapi.auth.sauth.token.TokenStorage;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
public class DefaultSAuthLogic implements SAuthLogic {

    private TokenStorage tokenStorage;

    public DefaultSAuthLogic(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @Override
    public boolean verify(String token) {
        return tokenStorage.exist(token);
    }
}
