package com.codingapi.tx.manager.core.restapi.auth;

import com.codingapi.tx.manager.core.restapi.auth.sauth.DefaultSAuthLogic;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
public class TxManagerAdminAuthLogic extends DefaultSAuthLogic {

    public TxManagerAdminAuthLogic(DefaultTokenStorage tokenStorage) {
        super(tokenStorage);
    }


    @Override
    public List<String> ignoreUrls() {
        return Arrays.asList("/admin/login", "/admin/index*", "/admin/js/*", "/admin/css/*", "/admin/assets/*",
                "/assets/*", "/error", "/manager/refresh", "/provider/*");
    }

}
