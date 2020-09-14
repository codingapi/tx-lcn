package com.codingapi.txlcn.tm.id;

import com.codingapi.txlcn.tm.MockRedisTemplateConfiguration;
import com.codingapi.txlcn.tm.node.TmNodeInitiator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.HashSet;
import java.util.Set;

import static com.codingapi.txlcn.tm.id.SnowflakeHandler.generateLogId;

/**
 * @author WhomHim
 * @date Create in 2020-8-10 22:09:30
 */
@SpringBootTest(classes = {MockRedisTemplateConfiguration.class, TmNodeInitiator.class})
public class SnowflakeHandlerTest {


    @Test
    public void id() {
        StopWatch stopWatch = new StopWatch();
        Set<Long> logSet = new HashSet<>();
        stopWatch.start();
        for (int i = 0; i < 1000; i++) {
            long logId = generateLogId();
            logSet.add(logId);
            System.out.println("logId:" + logId);
        }
        stopWatch.stop();
        Assert.isTrue(1000 == logSet.size(), "size must the same");
    }

}
