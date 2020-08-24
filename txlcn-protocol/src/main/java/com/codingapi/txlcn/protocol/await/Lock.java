package com.codingapi.txlcn.protocol.await;

import com.codingapi.txlcn.protocol.message.separate.AbsMessage;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public class Lock {

    private volatile AbsMessage res;

    private Condition condition;

    private java.util.concurrent.locks.Lock lock;

    private volatile boolean used = false;


    public void init() {
        used = true;
    }

    public void clear() {
        used = false;
        res = null;
    }


    public boolean isUsed() {
        return used;
    }

    public Lock() {
        lock = new ReentrantLock(true);
        condition = lock.newCondition();
    }


    public void await(long timeout) {
        try {
            lock.lock();
            try {
                condition.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            condition.signal();
        } finally {
            lock.unlock();
        }
    }


    public AbsMessage getRes() {
        synchronized (this) {
            return res;
        }
    }

    public void setRes(AbsMessage res) {
        synchronized (this) {
            this.res = res;
        }
    }

}
