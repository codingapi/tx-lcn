package com.codingapi.lcn.tx.sleuth2;

import brave.Span;
import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static brave.internal.HexCodec.writeHexLong;

/**
 * @author lorne
 * @date 2018/9/3
 * @description
 */
@Component
public class TracerHelper {

    @Autowired
    private Tracer tracer;

    public TracerContext getTracerContext() {
        TracerContext tracerContext = new TracerContext();
        Span span =  tracer.currentSpan();
        long traceIdHigh = span.context().traceIdHigh();
        long traceId = span.context().traceId();
        long spanId = span.context().spanId();
        boolean traceHi = traceIdHigh != 0;
        // 2 ids and the delimiter
        char[] result = new char[((traceHi ? 3 : 2) * 16) + 1];
        int pos = 0;
        if (traceHi) {
            writeHexLong(result, pos, traceIdHigh);
            pos += 16;
        }
        writeHexLong(result, pos, traceId);
        pos += 16;
        tracerContext.setTracerId(new String(Arrays.copyOfRange(result,0,pos)));
        int startLength = pos++;
        result[startLength] = '/';
        writeHexLong(result, pos, spanId);
        tracerContext.setSpanId(new String(Arrays.copyOfRange(result,startLength+1,result.length)));
        return tracerContext;
    }
}
