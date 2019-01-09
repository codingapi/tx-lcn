package com.codingapi.tx.spi.message.netty;

import com.codingapi.tx.spi.message.ClientInitCallBack;
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
@ConfigurationProperties("tx-lcn.message.netty")
@Data
public class RpcNettyConfiguration {


    /**
     * 最大等待时间 单位:(s)
     */
    private int waitTime = 5;

    /**
     * 最大缓存锁的数量
     */
    private int cacheSize = 1024;

    /**
     * appName 参数延迟删除时间 单位：(s)
     */
    private int attrDelayTime = 10;


    @Bean
    @ConditionalOnMissingBean
    public RpcAnswer rpcClientAnswer(){
        return rpcCmd -> log.info("cmd->{}",rpcCmd);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcLoadBalance rpcLoadBalance(){
        return new RandomLoadBalance();
    }


    @Bean
    @ConditionalOnMissingBean
    public ClientInitCallBack clientInitCallBack(){
        return remoteKey -> log.info("connected->{}",remoteKey);
    }

    @PostConstruct
    public void init(){
        RpcCmdContext.getInstance().setWaitTime(getWaitTime());
        RpcCmdContext.getInstance().setCacheSize(getCacheSize());
        SocketManager.getInstance().setAttrDelayTime(getAttrDelayTime());
    }
}
