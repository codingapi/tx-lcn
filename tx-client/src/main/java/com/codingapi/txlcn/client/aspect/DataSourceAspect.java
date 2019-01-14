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
package com.codingapi.txlcn.client.aspect;

import com.codingapi.txlcn.client.aspect.weave.DTXResourceWeaver;
import com.codingapi.txlcn.client.config.TxClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * create by lorne on 2018/1/5
 */
@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    private final TxClientConfig txClientConfig;

    private final DTXResourceWeaver dtxResourceWeaver;

    @Autowired
    public DataSourceAspect(TxClientConfig txClientConfig, DTXResourceWeaver dtxResourceWeaver) {
        this.txClientConfig = txClientConfig;
        this.dtxResourceWeaver = dtxResourceWeaver;
    }


    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return dtxResourceWeaver.around(point);
    }


    @Override
    public int getOrder() {
        return txClientConfig.getResourceOrder();
    }


}
