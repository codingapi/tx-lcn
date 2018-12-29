package com.example.demoe.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.common.dubbo.DDemoService;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.commons.util.Transactions;
import com.example.demoe.mapper.DDemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
@Service(
        version = "${demo.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
@Slf4j
public class DefaultDemoService implements DDemoService {

    @Autowired
    private DDemoMapper demoMapper;


    @Value("${spring.application.name}")
    private String appName;

    @Override
    @Transactional
    @TxTransaction(type = "txc")
    public String rpc(String name) {
        Demo demo = new Demo();
        demo.setDemoField(name);
        demo.setCreateTime(new Date());
        demo.setGroupId(TxTransactionLocal.current().getGroupId());
        demo.setAppName(appName);
        demo.setUnitId(TxTransactionLocal.current().getUnitId());
        demoMapper.save(demo);
        return "d-ok";
    }

}
