package com.codingapi.tx.framework.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * [类描述]
 *
 * @author caican
 * 17/9/12
 */
public class NamedThreadFactory implements ThreadFactory
{
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String mPrefix;

    private final boolean mDaemo;

    private final ThreadGroup mGroup;

    public NamedThreadFactory()
    {
        this("pool-" + POOL_SEQ.getAndIncrement(),false);
    }

    public NamedThreadFactory(String prefix)
    {
        this(prefix,false);
    }

    public NamedThreadFactory(String prefix, boolean daemo)
    {
        mPrefix = prefix + "-thread-";
        mDaemo = daemo;
        SecurityManager s = System.getSecurityManager();
        mGroup = ( s == null ) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable)
    {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup,runnable,name,0);
        ret.setDaemon(mDaemo);
        return ret;
    }

    public ThreadGroup getThreadGroup()
    {
        return mGroup;
    }
}
