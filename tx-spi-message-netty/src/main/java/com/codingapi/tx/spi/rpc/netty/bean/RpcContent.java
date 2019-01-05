package com.codingapi.tx.spi.rpc.netty.bean;

import com.codingapi.tx.spi.rpc.dto.MessageDto;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public class RpcContent {


    //默认值3秒
    private int seconds;

    private volatile MessageDto res;

    private Condition condition;

    private Lock lock;

    private volatile boolean used = false;


    public void init(){
        used = true;
    }

    public  void clear(){
        used = false;
        res = null;
    }


    public boolean isUsed() {
        return used;
    }

    public RpcContent(int seconds) {
        this.seconds = seconds;
        lock = new ReentrantLock(true);
        condition = lock.newCondition();
    }

    public void await(){
        try {
            lock.lock();
            try {
                condition.await(seconds, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal(){
        try {
            lock.lock();
            condition.signal();
        } finally {
            lock.unlock();
        }
    }


    public MessageDto getRes() {
        synchronized (this) {
            return res;
        }
    }

    public void setRes(MessageDto res) {
        synchronized (this) {
            this.res = res;
        }
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
