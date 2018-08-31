package com.codingapi.lcn.tx.sleuth2;

import brave.Tracer;
import com.codingapi.lcn.tx.api.sleuth.ISleuthTransactionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 * @date 2018/8/31
 * @description
 */
@Slf4j
@Service
public class SleuthTransactionApiImpl implements ISleuthTransactionApi {

    @Autowired
    private Tracer tracer;

    @Override
    public boolean isStart() {
        log.info("isStart");
        String tracerId = tracer.toString();
        String spanId =  tracer.currentSpan().toString();
        log.info("sleuth info ,spanId->{},tracerId->{}",spanId,tracerId);
        return false;
    }
}
