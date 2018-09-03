package com.codingapi.lcn.tx.sleuth2;

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
    private TracerHelper tracerHelper;

    @Override
    public boolean isStart() {
        log.info("isStart");
        TracerContext tracerContext = tracerHelper.getTracerContext();
        log.info("res->{}",tracerContext);
        String spanId = tracerContext.getSpanId();
        String tracerId = tracerContext.getTracerId();
        log.info("sleuth info ,spanId->{},tracerId->{}",spanId,tracerId);
        return spanId.equals(tracerId);
    }
}
