package com.codingapi.tx.client.springcloud.spi.message.netty;

import com.codingapi.tx.client.springcloud.spi.message.ClientInitCallBack;
import com.codingapi.tx.client.springcloud.spi.message.RpcAnswer;
import com.codingapi.tx.client.springcloud.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.tx.client.springcloud.spi.message.netty.bean.RpcCmdContext;
import com.codingapi.tx.client.springcloud.spi.message.netty.loadbalance.RandomLoadBalance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Configuration
@ComponentScan
@Slf4j
@Data
public class RpcNettyConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("tx-lcn.message.netty")
    public MessageConfig messageConfig() {
        return new MessageConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcAnswer rpcClientAnswer() {
        return rpcCmd -> log.info("cmd->{}", rpcCmd);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcLoadBalance rpcLoadBalance() {
        return new RandomLoadBalance();
    }


    @Bean
    @ConditionalOnMissingBean
    public ClientInitCallBack clientInitCallBack() {
        return remoteKey -> log.info("connected->{}", remoteKey);
    }

    @PostConstruct
    public void init() {
        RpcCmdContext.getInstance().setWaitTime(messageConfig().getWaitTime());
        RpcCmdContext.getInstance().setCacheSize(messageConfig().getCacheSize());
        SocketManager.getInstance().setAttrDelayTime(messageConfig().getAttrDelayTime());
    }
}
