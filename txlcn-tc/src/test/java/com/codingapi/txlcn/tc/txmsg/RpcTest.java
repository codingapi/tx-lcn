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
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.common.lock.DTXLocks;
import com.codingapi.txlcn.tc.MiniConfiguration;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.tc.txmsg.ReliableMessenger;
import com.codingapi.txlcn.tc.txmsg.TMSearcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 * Date: 19-1-23 上午10:52
 *
 * @author ujued
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MiniConfiguration.class)
public class RpcTest {

    @Autowired
    private ReliableMessenger messenger;

    @Test
    public void testLock() throws RpcException {

        // 第一个锁，True
        boolean result0 = messenger.acquireLocks("1", Sets.newSet("1"), DTXLocks.S_LOCK);
        Assert.assertTrue(result0);

        // 共享锁下可以加共享锁，　True
        boolean result1 = messenger.acquireLocks("2", Sets.newSet("1"), DTXLocks.S_LOCK);
        Assert.assertTrue(result1);

        //　共享锁下只准加共享锁，False
        boolean result = messenger.acquireLocks("3", Sets.newSet("1", "2"), DTXLocks.X_LOCK);
        Assert.assertFalse(result);

        messenger.releaseLocks(Sets.newSet("1"));

        // 锁被释放，True
        boolean result2 = messenger.acquireLocks("4", Sets.newSet("2", "3"), DTXLocks.X_LOCK);
        Assert.assertTrue(result2);

        // 同一个 DTX 下, True
        boolean result3 = messenger.acquireLocks("4", Sets.newSet("2"), DTXLocks.X_LOCK);
        Assert.assertTrue(result3);
    }

    /**
     * 多次重连TM Cluster
     */
    @Test
    public void testReconnect() {
        for (int i = 0; i < 100; i++) {
            TMSearcher.search();
        }
    }

    @Test
    public void testCountDown() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 110; i++) {
            System.out.println(countDownLatch.getCount());
            countDownLatch.countDown();
        }
    }

    @Test
    public void testCluster() throws RpcException {
        messenger.request(new MessageDto());
    }



}
