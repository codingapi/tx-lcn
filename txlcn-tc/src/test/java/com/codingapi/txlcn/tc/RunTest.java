package com.codingapi.txlcn.tc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * Date: 19-2-20 下午4:24
 *
 * @author ujued
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MiniConfiguration.class, TCAutoConfiguration.class})
public class RunTest {

    @Test
    public void testTxc() throws InterruptedException {
        Thread.sleep(10000);
    }
}
