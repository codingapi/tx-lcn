package com.codingapi.tx.logger;

import com.codingapi.tx.logger.db.LogDbProperties;
import com.codingapi.tx.logger.db.TxLoggerHelper;
import com.codingapi.tx.logger.db.TxLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
@Component
@Slf4j
public class DefaultTxLogger implements TxLogger {

    @Value("${spring.application.name}")
    private String appName;

    private final TxLoggerHelper txLoggerHelper;

    private final LogDbProperties dbProperties;

    private final ExecutorService executor;

    @Autowired
    public DefaultTxLogger(LogDbProperties dbProperties, TxLoggerHelper txLoggerHelper) {
        this.dbProperties = dbProperties;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.txLoggerHelper = txLoggerHelper;

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS");
        return format.format(new Date());
    }


    @Override
    public void trace(String groupId, String unitId, String tag, String content) {
        if (dbProperties.isEnabled()) {
            TxLog txLog = new TxLog();
            txLog.setContent(content);
            txLog.setGroupId(groupId);
            txLog.setTag(tag);
            txLog.setUnitId(Objects.isNull(unitId) ? "" : unitId);
            txLog.setAppName(appName);
            txLog.setCreateTime(getTime());
            log.debug("txLoggerInfoEvent->{}", txLog);
            this.executor.execute(() -> txLoggerHelper.insert(txLog));
        }
    }
}
