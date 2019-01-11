package com.codingapi.tx.spi.message.netty;

import com.codingapi.tx.spi.message.ClientInitCallBack;
import com.codingapi.tx.spi.message.RpcConfig;
import com.codingapi.tx.spi.message.RpcAnswer;
import com.codingapi.tx.spi.message.loadbalance.RpcLoadBalance;
import com.codingapi.tx.spi.message.netty.bean.RpcCmdContext;
import com.codingapi.tx.spi.message.netty.loadbalance.RandomLoadBalance;
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
    public RpcConfig rpcConfig() {
        return new RpcConfig();
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
        RpcCmdContext.getInstance().setWaitTime(rpcConfig().getWaitTime());
        RpcCmdContext.getInstance().setCacheSize(rpcConfig().getCacheSize());
        SocketManager.getInstance().setAttrDelayTime(rpcConfig().getAttrDelayTime());
    }
}
