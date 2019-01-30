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
package com.codingapi.txlcn.tm.support.restapi.auth;

import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.tm.support.restapi.auth.sauth.token.TokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
public class DefaultTokenStorage implements TokenStorage {

    private final FastStorage fastStorage;

    @Autowired
    public DefaultTokenStorage(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public boolean exist(String token) {
        try {
            List<String> tokens = fastStorage.findTokens();
            return tokens.contains(token);
        } catch (FastStorageException e) {
            return false;
        }
    }

    @Override
    public void add(String token) {
        try {
            fastStorage.saveToken(token);
        } catch (FastStorageException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void remove(String token) {
        try {
            fastStorage.removeToken(token);
        } catch (FastStorageException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void clear() {
        try {
            for (String s : fastStorage.findTokens()) {
                fastStorage.removeToken(s);
            }
        } catch (FastStorageException e) {
            throw new IllegalStateException(e);
        }
    }
}
