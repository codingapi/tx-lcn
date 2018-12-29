package com.example.demoe.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.example.common.db.domain.Demo;
import com.codingapi.example.common.dubbo.EDemoService;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.commons.annotation.TCCTransaction;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.spi.sleuth.TracerHelper;
import com.example.demoe.mapper.EDemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

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
public class DefaultDemoService implements EDemoService {

    @Autowired
    private EDemoMapper demoMapper;

    private ConcurrentHashMap<String, Long> ids = new ConcurrentHashMap<>();

    @Value("${spring.application.name}")
    private String appName;

    @Override
    @Transactional
    @TxTransaction(type = "tcc")
    @TCCTransaction(confirmMethod = "cm", cancelMethod = "cl", executeClass = DefaultDemoService.class)
    public String rpc(String name) {
        Demo demo = new Demo();
        demo.setDemoField(name);
        demo.setCreateTime(new Date());
        demo.setGroupId(TxTransactionLocal.current().getGroupId());
        demo.setUnitId(TxTransactionLocal.current().getUnitId());
        demo.setAppName(appName);
        demoMapper.save(demo);
        ids.put(TxTransactionLocal.current().getGroupId(), demo.getId());
        return "e-ok";
    }

    public void cm(String name) {
        log.info("tcc-confirm-" + TxTransactionLocal.getOrNew().getGroupId());
        ids.remove(TxTransactionLocal.getOrNew().getGroupId());
    }

    public void cl(String name) {
        log.info("tcc-cancel-" + TxTransactionLocal.getOrNew().getGroupId());
        demoMapper.deleteById(ids.get(TxTransactionLocal.getOrNew().getGroupId()));
    }
}
