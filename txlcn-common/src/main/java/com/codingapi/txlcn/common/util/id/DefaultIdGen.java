package com.codingapi.txlcn.common.util.id;

/**
 * Description:
 * Date: 19-1-31 下午7:32
 *
 * @author ujued
 */
public class DefaultIdGen implements IdGen {

    private int machineId;

    private int machineOffset;

    private int sequence;

    private long lastStamp = -1L;

    private int maxSequenceValue;

    private int timeOffset;

    /**
     * 起始的时间戳 UTC+8:00 2019-01-01 0:0:0 000
     */
    private final static long START_STAMP = 1546272000000L;

    public DefaultIdGen(int machineLen, int sequenceLen, int machineId) {
        this.maxSequenceValue = ~(-1 << sequenceLen);
        this.machineOffset = sequenceLen;
        this.timeOffset = machineOffset + machineLen;
        this.machineId = machineId;
    }

    @Override
    public long nextId() {
        long currentStamp = curTime();
        if (currentStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.");
        }

        if (currentStamp == lastStamp) {
            sequence = (sequence + 1) & this.maxSequenceValue;
            if (sequence == 0) {
                lastStamp = tilNextMillis();
            }
        } else {
            sequence = 0;
        }
        lastStamp = currentStamp;
        return (currentStamp - START_STAMP) <<
                timeOffset | machineId << machineOffset | sequence;
    }

    private long curTime() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis() {
        long timestamp = curTime();
        while (timestamp <= lastStamp) {
            timestamp = curTime();
        }
        return timestamp;
    }
}
