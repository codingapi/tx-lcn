package com.codingapi.txlcn.tracing.http.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@EnableConfigurationProperties(LoadbalancerConfiguration.class)
@ConditionalOnClass(IRule.class)
@ConfigurationProperties("tx-lcn.ribbon.loadbalancer.dtx")
@ConditionalOnProperty(name = "tx-lcn.ribbon.loadbalancer.dtx.enabled", matchIfMissing = true)
public class LoadbalancerConfiguration {
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
        return new TxlcnZoneAvoidanceRule(registration);
    }
}
