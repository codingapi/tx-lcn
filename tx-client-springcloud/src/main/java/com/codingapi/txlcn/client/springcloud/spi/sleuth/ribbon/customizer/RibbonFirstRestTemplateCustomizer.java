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
