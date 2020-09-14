package com.codingapi.txlcn.tc.id;

import com.codingapi.txlcn.protocol.message.event.SnowflakeCreateEvent;
import com.codingapi.txlcn.tc.cache.Cache;
import com.codingapi.txlcn.tc.constant.SnowflakeConstant;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-8-15 22:55:46
 */
@Slf4j
public class SnowflakeInfo implements SnowflakeStep {

    private TxManagerReporter txManagerReporter;

    public SnowflakeInfo(TxManagerReporter txManagerReporter) {
        this.txManagerReporter = txManagerReporter;
    }

    @Override
    public void getGroupIdAndLogId() {
        // 是否请求过 TM 的标志位
        if (Cache.getKey(SnowflakeConstant.TAG) == null) {
            log.debug("==> getGroupIdAndLogId");
            // 请求 TM 获得全局唯一 Id
            SnowflakeCreateEvent snowFlakeCreateEvent =
                    (SnowflakeCreateEvent) txManagerReporter.requestMsg(new SnowflakeCreateEvent());
            log.debug("==> snowFlakeCreateEvent:{}", snowFlakeCreateEvent);
            if (snowFlakeCreateEvent != null) {
                Cache.setKey(SnowflakeConstant.GROUP_ID, snowFlakeCreateEvent.getGroupId());
                Cache.setKey(SnowflakeConstant.LOG_ID, snowFlakeCreateEvent.getLogId());
                Cache.setKey(SnowflakeConstant.TAG, true);
            }

        }

    }
}
