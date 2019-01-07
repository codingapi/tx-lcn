package com.example.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.common.db.mapper.DemoMapper;
import com.codingapi.example.common.dubbo.DDemoService;
import com.codingapi.example.common.dubbo.EDemoService;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.example.service.DemoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/21
 *
 * @author codingapi
 */
@Service
public class DemoApiServiceImpl implements DemoApiService {

    @Reference(version = "${demo.service.version}",
            application = "${dubbo.application.d}",
            registry = "${dubbo.registry.address}",
            retries = -1,
            loadbalance = "txlcn_random")
    private DDemoService dDemoService;

    @Reference(version = "${demo.service.version}",
            application = "${dubbo.application.e}",
            retries = -1,
            registry = "${dubbo.registry.address}",
            loadbalance = "txlcn_random")
    private EDemoService eDemoService;

    @Autowired
    private DemoMapper demoMapper;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    @TxTransaction
    @Transactional
    public String execute(String name) {
        String dResp = dDemoService.rpc(name);
        String eResp = eDemoService.rpc(name);
        Demo demo = new Demo();
        demo.setCreateTime(new Date());
        demo.setAppName(appName);
        demo.setDemoField(name);
        demo.setGroupId(DTXLocal.cur().getGroupId());
        demo.setUnitId(DTXLocal.cur().getUnitId());
        demoMapper.save(demo);
        int a = 1 / 0;
        return dResp + " > " + eResp + " > " + "client-ok";
    }
}
