package com.codingapi.txlcn.common.util.id;

/**
 * Description:
 * Date: 2/1/19
 *
 * @author ujued
 */
public class DefaultIdGen implements IdGen {

    /**
     * 2019-2-1
     */
    private static final long START_TIME = 1548992829394L;

    private long machineId;

    private int machineOffset;

    private long seq;

    private int seqLen;

    private long lastTime;

    public DefaultIdGen(int seqLen, long machineId) {
        this.seqLen = seqLen;
        this.machineOffset = seqLen;
        this.machineId = machineId;
    }

    @Override
    public synchronized String nextId() {
        long curTime = System.currentTimeMillis();
        if (curTime < lastTime) {
            throw new IllegalStateException("");
        }

        if (curTime == lastTime) {
            seq = (seq + 1) & (~(-1 << seqLen));
            if (seq == 0L) {
                curTime = tilNextMillis();
            }
        } else {
            seq = 0L;
        }

        lastTime = curTime;

        long seqWithMachine = (machineId << machineOffset & Long.MAX_VALUE) | seq;

        return Long.toHexString((curTime - START_TIME) ^ 9527) + Long.toHexString(seqWithMachine ^ 9527);
    }

    private long tilNextMillis() {
        long newTime = System.currentTimeMillis();
        while (newTime <= lastTime) {
            newTime = System.currentTimeMillis();
        }
        return newTime;
    }
}
