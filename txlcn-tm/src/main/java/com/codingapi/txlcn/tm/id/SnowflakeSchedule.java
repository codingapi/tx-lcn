package com.codingapi.txlcn.tm.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author WhomHim
 * @description 24小时重新设置过期时间一次
 * @date Create in 2020-8-17 22:34:28
 */
@Component
public class SnowflakeSchedule {
    @Autowired
    private SnowflakeInitiator snowflakeInitiator;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 23)
    private void snowflakeInitiatorResetExpire() {
        snowflakeInitiator.resetExpire();
    }


}
