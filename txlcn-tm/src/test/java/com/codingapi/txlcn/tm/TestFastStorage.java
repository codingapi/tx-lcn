package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void testMacId() throws FastStorageException {

    }
}
