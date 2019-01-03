package com.codingapi.tx.manager.support.txex.provider;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.manager.db.domain.TxException;
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
    public DefaultExUrlProvider() {
        this.javaMailSender = null;
        this.mailProperties = null;
    }

    @Autowired(required = false)
    public DefaultExUrlProvider(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
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
