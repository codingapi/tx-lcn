package com.codingapi.txlcn.client.springcloud.spi.sleuth.ribbon.customizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: set loadBalancerInterceptor to first.
 * Company: CodingApi
 * Date: 2018/12/18
 *
 * @author codingapi
 */
@Component
public class RibbonFirstRestTemplateCustomizer implements RestTemplateCustomizer {


    @Autowired(required = false)
    private LoadBalancerInterceptor loadBalancerInterceptor;

    @Autowired(required = false)
    private RetryLoadBalancerInterceptor retryLoadBalancerInterceptor;


    @Override
    public void customize(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
        if(loadBalancerInterceptor!=null) {
            list.add(0, loadBalancerInterceptor);
        }
        if(retryLoadBalancerInterceptor!=null){
            list.add(0, retryLoadBalancerInterceptor);
        }
        restTemplate.setInterceptors(list);
    }

}
