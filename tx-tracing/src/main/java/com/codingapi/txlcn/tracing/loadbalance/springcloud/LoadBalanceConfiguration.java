package com.codingapi.txlcn.tracing.loadbalance.springcloud;

import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Description:
 * Date: 19-1-29 上午9:37
 *
 * @author ujued
 */
@Configuration
@EnableConfigurationProperties(LoadBalanceConfiguration.class)
@ConfigurationProperties(prefix = "tx-lcn.springcloud.loadbalance")
@ConditionalOnProperty(name = "tx-lcn.springcloud.loadbalance.enabled", havingValue = "true")
public class LoadBalanceConfiguration {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope("prototype")
    public IRule ribbonRule(Registration registration) {
        return new TXLCNZoneAvoidanceRule(registration);
    }
}
