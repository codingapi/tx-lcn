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
package com.codingapi.txlcn.manager.support.txex.provider;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.db.domain.TxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Description:
 * Date: 19-1-3 上午9:54
 *
 * @author ujued
 */
@RestController
@Slf4j
public class DefaultExUrlProvider {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    @Autowired(required = false)
    public DefaultExUrlProvider(TxManagerConfig txManagerConfig) {
        this(null, null, txManagerConfig);
    }

    @Autowired(required = false)
    public DefaultExUrlProvider(JavaMailSender javaMailSender,
                                MailProperties mailProperties,
                                TxManagerConfig txManagerConfig) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
        if (Objects.isNull(javaMailSender)) {
            if (txManagerConfig.getExUrl().contains("ujued@qq.com")) {
                txManagerConfig.setExUrlEnabled(false);
            }
        }
    }

    @PostMapping("/provider/email-to/{email}")
    public boolean email(@PathVariable("email") String email, @RequestBody TxException txEx) {
        if (Objects.isNull(javaMailSender)) {
            log.error("non admin mail configured. so tx exception not send.");
            return false;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(email);
        message.setSubject("TX-LCN Transaction Exception!");
        message.setText(JSON.toJSONString(txEx));
        javaMailSender.send(message);
        return true;
    }
}
