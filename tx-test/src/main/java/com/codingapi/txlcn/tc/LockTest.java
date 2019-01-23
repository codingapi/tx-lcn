package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.commons.lock.DTXLocks;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.tc.message.ReliableMessenger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * Date: 19-1-23 上午10:52
 *
 * @author ujued
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TCAutoConfiguration.class, MiniConfiguration.class})
public class LockTest {

    @Autowired
    private ReliableMessenger messenger;

    @Test
    public void testLock() throws RpcException {
        boolean result = messenger.acquireLocks(Sets.newSet("1", "2"), DTXLocks.X_LOCK);
        System.out.println(result);

        boolean result2 = messenger.acquireLocks(Sets.newSet("2", "3"), DTXLocks.X_LOCK);
        System.out.println(result2);
//        boolean result2 = messenger.releaseLock("1", "24854854ds4d4s5d45s4d5");
//        System.out.println(result2);
    }

}
