package com.codingapi.txlcn.tm.node;

import com.codingapi.txlcn.tm.reporter.TmManagerReporter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author WhomHim
 * @description 30秒重新设置过期时间一次，相当于心跳一样告诉其他 TM “我还存活”
 * @date Create in 2020-8-17 22:34:28
 */
@Component
public class TmNodeSchedule {

    private final TmNodeInitiator tmNodeInitiator;

    private final TmManagerReporter tmManagerReporter;

    public TmNodeSchedule(TmNodeInitiator tmNodeInitiator, TmManagerReporter tmManagerReporter) {
        this.tmNodeInitiator = tmNodeInitiator;
        this.tmManagerReporter = tmManagerReporter;
    }

    @Scheduled(cron = "0/30 * * * * ?")
    private void snowflakeInitiatorResetExpire() {
        tmNodeInitiator.resetExpire(tmManagerReporter);
    }


}
