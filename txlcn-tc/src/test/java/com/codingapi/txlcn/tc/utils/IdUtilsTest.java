package com.codingapi.txlcn.tc.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.HashSet;
import java.util.Set;

import static com.codingapi.txlcn.tc.utils.IdUtils.generateLogId;

/**
 * @author WhomHim
 * @date Create in 2020-8-10 22:09:30
 */
public class IdUtilsTest {

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
