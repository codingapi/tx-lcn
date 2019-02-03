package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.lock.DTXLocks;
import com.codingapi.txlcn.tm.core.storage.LockValue;
import com.codingapi.txlcn.tm.core.storage.redis.RedisStorage;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TMApplication.class)
public class RedisStorageTest {


    @Autowired
    private RedisStorage redisStorage;

    String contextId = "A";
    Set<String> locks = Sets.newHashSet("1","2");
    LockValue lockValue = new LockValue();

    @Test
    public void acquireLocks(){
        ExecutorService service = Executors.newFixedThreadPool(100);

        for(int i=0;i<1000;i++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    lockValue.setLockType(DTXLocks.X_LOCK);
                    try {
                        redisStorage.acquireLocks(contextId,locks,lockValue);
                        System.out.println("success");
                    } catch (FastStorageException e) {
                        System.out.println("fail");
                    }
                }
            });
        }

        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
