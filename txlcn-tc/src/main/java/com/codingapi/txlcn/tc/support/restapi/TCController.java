package com.codingapi.txlcn.tc.support.restapi;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 袁永君
 * @date 2019/8/6 17:58
 */
@Slf4j
@RestController
public class TCController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/notify/reconnect")
    public Boolean reconnect() throws Exception{
        log.info("客户端重新连接tx-manager");
        Map<String, TxLcnInitializer> runnerMap = applicationContext.getBeansOfType(TxLcnInitializer.class);
        List<TxLcnInitializer> initializers = runnerMap.values().stream().sorted(Comparator.comparing(TxLcnInitializer::order))
                .collect(Collectors.toList());

        for (TxLcnInitializer txLcnInitializer : initializers) {
            txLcnInitializer.init();
        }
        return true;
    }
}
