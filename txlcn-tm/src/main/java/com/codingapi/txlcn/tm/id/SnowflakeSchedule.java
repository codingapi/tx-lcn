package com.codingapi.txlcn.tm.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author WhomHim
 * @description 30秒重新设置过期时间一次，相当于心跳一样告诉其他 TM “我还存活”
 * @date Create in 2020-8-17 22:34:28
 */
@Component
public class SnowflakeSchedule {

    @Autowired
    private SnowflakeInitiator snowflakeInitiator;

    @Scheduled(cron = "0/30 * * * * ?")
    private void snowflakeInitiatorResetExpire() {
        snowflakeInitiator.resetExpire();
    }


}
