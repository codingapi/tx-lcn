/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.spi.message.netty.bean;

import com.codingapi.txlcn.spi.message.dto.MessageDto;

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
