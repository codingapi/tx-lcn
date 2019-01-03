package com.codingapi.tx.client.aspectlog;

import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.util.serializer.SerializerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description: 切面日志的异步操作
 * Date: 2018/12/28
 *
 * @author ujued
 * @see AspectLogger
 */
@Component
@Slf4j
public class AsyncH2DBAspectLogger implements AspectLogger {

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    private final AspectLogHelper txLogHelper;

    @Autowired
    public AsyncH2DBAspectLogger(AspectLogHelper txLogHelper) {
        this.txLogHelper = txLogHelper;

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    @Override
    public void trace(String groupId, String unitId, TransactionInfo transactionInfo) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            log.debug("event-save-start->{}", groupId);
            byte[] bytes;
            try {
                bytes = SerializerContext.getInstance().serialize(transactionInfo);
            } catch (SerializerException e) {
                e.printStackTrace();
                return;
            }
            AspectLog txLog = new AspectLog();
            txLog.setBytes(bytes);
            txLog.setGroupId(groupId);
            txLog.setUnitId(unitId);
            txLog.setMethodStr(transactionInfo.getMethodStr());
            txLog.setTime(System.currentTimeMillis());
            txLog.setUnitIdHash(groupId.hashCode());
            txLog.setUnitIdHash(unitId.hashCode());

            boolean res = txLogHelper.save(txLog);
            long t2 = System.currentTimeMillis();
            log.debug("event-save-over ok:{} ->{},time:{}", res, groupId, (t2 - t1));
        });
    }

    @Override
    public void clearLog(String groupId, String unitId) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            log.debug("event-clear-start->{}", groupId);
            boolean res = txLogHelper.delete(groupId.hashCode(), unitId.hashCode());
            long t2 = System.currentTimeMillis();
            log.debug("event-clear-over ok:{} ->{},time:{}", res, groupId, (t2 - t1));
        });
    }
}
