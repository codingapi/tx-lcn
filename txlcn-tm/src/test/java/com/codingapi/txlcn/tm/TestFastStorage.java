package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * Date: 19-1-31 上午10:26
 *
 * @author ujued
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TMApplication.class)
public class TestFastStorage {

    @Autowired
    private FastStorage fastStorage;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> stringStringRedisTemplate;

    @Test
    public void testMacId() throws FastStorageException {

    }

    @Test
    public void testMutiSetIf() {
        Boolean result = redisTemplate.opsForValue().multiSetIfAbsent(Maps.newHashMap("a", 1, "b", 2, "c", 3));
        System.out.println(result);
    }

    @Test
    public void testRedisInc(){
        stringStringRedisTemplate.opsForValue().setIfAbsent("o", "1");
        stringStringRedisTemplate.opsForValue().increment("o", 1);
        System.out.println(stringStringRedisTemplate.opsForValue().get("o"));

    }
}
