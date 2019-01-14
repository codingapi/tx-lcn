package com.codingapi.txlcn.manager.support.restapi.auth.sauth;


import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @auther ujued
 */
public interface SAuthLogic {

    default List<String> ignoreUrls() {
        return Collections.emptyList();
    }

    default boolean isIgnored(HttpServletRequest request) throws SAuthHandleException {
        return false;
    }

    boolean verify(String token);
}
